Lambda Service
==============

Purpose
-------

This is a rest service that deals with "Things".

Important Notes
---------------

This project will also deploy a similarly versioned "lambda-service-model" project that contains the DTOs for the 
project for compatibility.

Project Layout
--------------

 * The `com.thoughtworks.etwg.lambdaservice.things` package contains all the things that deal with "Things"
 * `com.thoughtworks.etwg.lambdaservice.ApplicationKt.main` runs the application 
 * `com.thoughtworks.etwg.lambdaservice.Handler` is the AWS lambda handler
 
Running Locally
---------------

 * Make sure `modules/lambda-service/src/main/resources/application-local.yml` is correct
 * Run `SPRING_PROFILES_ACTIVE=local ./gradlew :modules:lambda-service:springBootRun`
 
 Packaging
 ---------
 
 This project produces two Jar files, one a standard SpringBoot jar, and a shadow jar. The shadow jar contains all the 
 dependencies unrolled into packages and excludes the Tomcat web server libraries, considered suitable for deployment
 to the AWS lambda environment.