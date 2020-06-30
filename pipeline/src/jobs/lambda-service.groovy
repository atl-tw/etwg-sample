pipelineJob('lambda-service') {
    def repo = 'git@github.com:atl-tw/etwg-sample.git'
    triggers {
        scm('H/5 * * * *')
    }
    description("Lambda Service Build")
    definition {
        cpsScm {
            scm {
                git {
                    remote { url(repo) }
                    branches('master', '**/feature*')
                    scriptPath('modules/lambda-service/Jenkinsfile')
                    extensions { }  // required as otherwise it may try to tag the repo, which you may not want
                }
            }
        }
    }
}