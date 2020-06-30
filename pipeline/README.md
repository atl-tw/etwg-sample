Pipeline
========

Purpose
-------

This project contains configuration and operations for deploying, maintaining, and executing things that are specific
to the build pipeline(s) for the project as a whole.

Prerequisites
-------------

You need a Jenkins server and a Maven repository server, and valid admin credentials for each.


Running/Job Setup
-----------------

 1. Ensure the settings in `gradle.properties` are correct.
 2. Run `./gradlew deploySecrets -PjenkinsUsername=XXX -PjenkinsPassword=YYY
  -PmavenRepositoryUsername=AAA -PmavenRepositoryPaswsword` to deploy the maven repo secret
 3. Run `./gradlew -PjenkinsUsername=XXX -PjenkinsPassword=YYY pipeline:publishJenkinsJobs` to create/update the Jenkins jobs.
 
Project Structure
-----------------

 * `src/main/groovy`  Groovy classes for interacting with the Jenkins API
 * `src/jobs` The JobDSL files for configuring the jobs. These should be kept as thin as possibe. The primary build
   configuration is configured in the `Jenkinsfile` for individual modules. Ideally you should be able to deploy these
   configurations once and forget about them, but rather use the `Jenkinsfiles` for actually controlling your build. 
 * `src/xml` XML templates.  
 
 
 

 