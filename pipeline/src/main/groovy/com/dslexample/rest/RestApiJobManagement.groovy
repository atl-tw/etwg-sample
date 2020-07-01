package com.dslexample.rest

import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import javaposse.jobdsl.dsl.*
import org.apache.http.util.EntityUtils

class RestApiJobManagement extends MockJobManagement {

    private boolean creds = false;
    final createCred = 'credentials/store/system/domain/_/createCredentials'
    final createITem = 'createItem'

    final RESTClient restClient
    private boolean crumbHeaderSet = false

    RestApiJobManagement(String baseUrl) {
        restClient = new RESTClient(baseUrl)
        restClient.handler.failure = { it }
    }

    void setCredentials(String username, String password) {
        crumbHeaderSet = false
        restClient.headers['Authorization'] = 'Basic ' + "$username:$password".bytes.encodeBase64()
    }

    void setCreds(boolean value){
        this.creds = value;
    }

    @Override
    String getConfig(String jobName) throws JobConfigurationNotFoundException {
        String xml = fetchExistingXml(jobName)
        if (!xml) {
            throw new JobConfigurationNotFoundException(jobName)
        }

        xml
    }

    @Override
    boolean createOrUpdateConfig(Item item, boolean ignoreExisting) throws NameNotProvidedException {
        createOrUpdateConfig(item.name, item.xml, ignoreExisting, false)
    }

    @Override
    void createOrUpdateView(String viewName, String config, boolean ignoreExisting) throws NameNotProvidedException, ConfigurationMissingException {
        createOrUpdateConfig(viewName, config, ignoreExisting, true)
    }

    boolean createOrUpdateConfig(String name, String xml, boolean ignoreExisting, boolean isView) throws NameNotProvidedException {
        boolean success
        String status

        String existingXml = fetchExistingXml(name, isView)
        if (existingXml) {
            if (ignoreExisting) {
                success = true
                status = 'ignored'
            } else {
                success = update(name, xml, isView)
                status = success ? 'updated' : 'update failed'
            }
        } else {
            success = create(name, xml, isView)
            status = success ? 'created' : 'create failed'
        }

        println "$name - $status"
        success
    }

    @Override
    InputStream streamFileInWorkspace(String filePath) throws IOException {
        new File(filePath).newInputStream()
    }

    @Override
    String readFileInWorkspace(String filePath) throws IOException {
        new File(filePath).text
    }

    boolean create(String name, String xml, boolean isView) {
        String job
        String path
        if (name.contains('/')) {
            int index = name.lastIndexOf('/')
            String folder = name[0..(index - 1)]
            job = name[(index + 1)..-1]
            path = getPath(folder, isView) + '/'+ (creds? createCred : createITem)
        } else {
            job = name
            path = isView ? 'createView' : (creds ? createCred : createITem)
        }

        setCrumbHeader()
        HttpResponseDecorator resp = restClient.post(
            path: path,
            body: xml,
            query: [name: job],
            requestContentType: 'application/xml'
        )
        if(resp.status > 202) {
            println "$resp.status path $path"
            println EntityUtils.toString(resp.entity);
        }
        resp.status == 200
    }

     boolean update(String name, String xml, boolean isView) {
        setCrumbHeader()
        HttpResponseDecorator resp = restClient.post(
            path: getPath(name, isView) + '/config.xml',
            body: xml,
            requestContentType: 'application/xml'
        )

        resp.status == 200
    }

    String fetchExistingXml(String name, boolean isView) {
        setCrumbHeader()
        try {
            HttpResponseDecorator resp = restClient.get(
                    contentType: ContentType.TEXT,
                    path: getPath(name, isView) + '/config.xml',
                    headers: [Accept: 'application/xml'],
            )
            resp?.data?.text
        } catch(NoSuchMethodError e){
            //TODO This a hack to get around a bug with groovy 3 and http-builder.
            return "temp"
        }
     }

    static String getPath(String name, boolean isView) {
        if (name.startsWith('/')) {
            return '/' + getPath(name[1..-1], isView)
        }
        isView ? "view/$name" : "job/${name.replaceAll('/', '/job/')}"
    }

    private setCrumbHeader() {
        if (crumbHeaderSet)
            return

        HttpResponseDecorator resp = restClient.get(path: 'crumbIssuer/api/xml')
        if (resp.status == 200) {
            restClient.headers[resp.data.crumbRequestField] = resp.data.crumb
        }
        crumbHeaderSet = true
    }
}
