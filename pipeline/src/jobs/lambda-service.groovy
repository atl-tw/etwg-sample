pipelineJob('lambda-service') {
    triggers {
        scm('*/5 * * * *')
    }
    description("Lambda Service Build")
    definition {
        cpsScm {
            scm {
                git {
                    remote { url('git@github.com:atl-tw/etwg-sample.git') }
                    branches('master', '**/feature*')
                    scriptPath('modules/lambda-service/Jenkinsfile')
                    extensions {
                        extensions {
                            cleanBeforeCheckout()
                            disableRemotePoll() // this is important for path restrictions to work
                            configure { git ->
                                git / 'extensions' / 'hudson.plugins.git.extensions.impl.PathRestriction' {
                                    includedRegions "modules/lambda-service/.*\nmodules/lambda-service-model/.*\nbuild\\.gradle"
                                    excludedRegions ".*\\.md\n\\.gitignore"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}