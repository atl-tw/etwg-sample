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
//                        'hudson.plugins.git.extensions.impl.SparseCheckoutPaths' {
//                            sparseCheckoutPaths {
//                                'hudson.plugins.git.extensions.impl.SparseCheckoutPath' {
//                                    path '.*\\.*'
//                                }
//                                'hudson.plugins.git.extensions.impl.SparseCheckoutPath' {
//                                    path 'gradle/'
//                                }
//                                'hudson.plugins.git.extensions.impl.SparseCheckoutPath' {
//                                    path 'pipeline/'
//                                }
//                            }
//                        }

                    }

                }
            }
        }
    }
}