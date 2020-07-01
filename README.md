(This is a template project. See [the docs](./docs/index.md))

Some Project
============


Purpose
-------

This project contains the service that does the service-like thing and the necessary infrastructure to maintain it.


Getting Started
---------------

### Prerequisites

 * Some modern version of Java (11+)
 * If you have some older version of Java (8+), you can run `./gradlew setupJava` and it will install OpenJDK 11 using `javaenv`
   into the `./.gradle/javaenv/versions` folder. You can then run `export JAVA_HOME=$(./.gradle/javaenv/javaenv home)`
 
### Setup

To set up the project, run 

```bash
./gradlew setup
```

This will install the following into the .gradle folder:

 * gradle
 * pyenv
 * python 3.7
 * awscli
 * terraform
 * jq
 
You will also get a git `pre-commit` hook that formats source files and applies 
the license header located in `./etc/HEADER`

Running Locally
---------------

You can run the `lambda-service` module like this:
``SPRING_PROFILES_ACTIVE=local ./gradlew - :modules:lambda-service:bootRun``

Because the lambda outputs JSON-Line formatted logs, it can be hard to read. You can make this easier to read using:
``SPRING_PROFILES_ACTIVE=local ./gradlew --console=plain :modules:lambda-service:bootRun | ./bin/logformat``

Be sure to consult the application-local.yml file for configuration.

Project Structure
-----------------

 * modules
   * [lambda-service](./modules/lambda-service/README.md): the service implementation
   * [lambda-service-model](./modules/lambda-service-model/README.md): a shared model package for consumers
* [infrastructure](./infrastructure/README.md): scripts and terraform to deploy the application
* [pipeline](./pipeline/README.md): scripts to deploy and configure the build pipeline
* [docs](./docs/index.md): documentation

Project Quality Standards
-------------------------

All Java or Kotlin projects must pass the following global checks:

 * [JaCoCo](https://www.eclemma.org/jacoco/) Test Coverage of 85%. See the root `build.gradle` and search for 
   "jacocoTestCoverageVerification".
 * [SpotBugs](https://spotbugs.github.io/) and [FindSecBugs](https://find-sec-bugs.github.io/). 
   See `build/reports/spotbugs.html`
 * [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/). Exclusions that are build related are in 
   `etc/suppression.html`. If you need to clobber a
   dependency globally, see the root `build.gradle` file and search for "security vulnerabilities".
 * [License Formatting](https://github.com/hierynomus/license-gradle-plugin). Should include the header from `etc/HEADER`.
   Run `./gradlew licenseFormat` to apply, or use the
   default git pre-commit hook. (NB: This plugin uses Spring, which requires a global buildscript sprint dependency 
   in the root that should be migrated if you change Spring Boot versions significantly.)
 * [Spotless](https://github.com/diffplug/spotless) Java/Spotless Kotlin Check. Run `./gradlew spotlessApply` to apply, 
   or use the default git pre-commit hook.
 * PMD with the following rulesets:
    * [best practices](https://pmd.github.io/latest/pmd_rules_java_bestpractices.html)
    * [error_prone](https://pmd.github.io/latest/pmd_rules_java_errorprone.html) (This will mostly overlap with spotbugs)
    * [security](https://pmd.github.io/latest/pmd_rules_java_errorprone.html)
    * [performance](https://pmd.github.io/latest/pmd_rules_java_performance.html)
 
 NB: If you add a new Java/Kotlin project to the tree, please be sure to add it to the `java_kotlin_projects` list in 
 the root `build.gradle` file so the standard checks will be applied to it.
 
 All of these items might require occasional care and feeding -- version bumps, configuration changes, etc -- but they 
 are mostly all evergreen elements of a project.
    
You can omit a class or method from coverage validation by using the ``@NoCoverageGenerated(justification = "Something")`` 
annotation. Similarly, you can omit a class or method from SpotBugs using 
``@SuppressFBWarnings("code", justification = "Something")``. You can also suppress a PMD warning by using 
``@SuppressWarnings("PMD.RuleName")``.

You should have a good reason for these. Please avail yourself of the "justification" field 

You can find reports on each of these in the ``build/reports`` directory of each individual module.

What to Do If You See a Security Vulnerability
----------------------------------------------

 1. Is it a "JAR" dependency?
     1. Validate that it is real. Sometimes there are things that are false positives. There is one currently noted in the 
        project at the time of this writing. If it is a false positive, add it to the `./etc/suppression.xml` file and make 
        a note of it.
     1. As false positives in the NVD are occasionally ( :/ ) fixed, it is a good idea to remove the suppression file 
        and make sure the list is applicable.
     1. If it is not a false positive, search in the root `build.gradle` file for "dependencies for security 
        vulnerabilities". There is a block of sample code that you can use to forcibly replace the offending dependency 
        across all the projects in the structure.
 1. Is it a "Docker Layer" dependency?
    1. TBD.
    
Creating a New Module
---------------------

