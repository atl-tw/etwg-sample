pipelineJob('lambda-service') {
    properties {
        disableConcurrentBuilds()
    }
    triggers {
        scm('H/5 * * * *')
    }
    description("Lambda Service Build")
    definition {
        cpsScm {
            scm {
                git {
                    remote { url('git@github.com:atl-tw/etwg-sample.git') }
                    branches('master', '**/feature*')
                    scriptPath('modules/lambda-service/Jenkinsfile')
                    extensions { }  // required as otherwise it may try to tag the repo, which you may not want
                }
            }
        }
    }
}