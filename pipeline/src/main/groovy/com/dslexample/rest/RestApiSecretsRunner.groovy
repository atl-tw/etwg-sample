package com.dslexample.rest


String file = System.getProperty('file')
String baseUrl = System.getProperty('baseUrl')
String secret = System.getProperty('secret')
String username = System.getProperty('username')
String password = System.getProperty('password') // password or token

if (!file || !baseUrl) {
    println 'usage: -Dpattern=<pattern> -DbaseUrl=<baseUrl> [-Dusername=<username>] [-Dpassword=<password>]'
    System.exit 1
}

RestApiJobManagement management = new RestApiJobManagement(baseUrl);
if(username != null){
    management.setCredentials(username, password)
}
management.setCreds(true)
String xml = new File(file).text;
management.create(secret, xml,  false);
