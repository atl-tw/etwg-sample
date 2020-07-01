pipelineJob('master-job') {
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
                    scriptPath('pipeline/Jenkinsfile')
                    extensions {
                        cleanBeforeCheckout()
                        disableRemotePoll() // this is important for path restrictions to work
                        configure { git ->
                            git / 'extensions' / 'hudson.plugins.git.extensions.impl.PathRestriction' {
                                includedRegions "pipeline/.*"
                                excludedRegions "README.md\n\\.gitignore\npom.xml"
                            }
                        }
                    }
                }
            }
        }
    }
}