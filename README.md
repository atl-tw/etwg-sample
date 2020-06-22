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
 
### Setup

To set up the project, run 

```bash
./gradlew setup
```

This will install the following into the .gradle folder:

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
   * lambda-service: the service implementation
   * lambda-service-model: a shared model package for consumers
* infrastructure: scripts and terraform to deploy the application
* docs: documentation

Project Quality Standards
-------------------------

All Java or Kotlin projects must pass the following global checks:

 * JaCoCo Test Coverage of 85%. See the root `build.gradle` and search for "jacocoTestCoverageVerification".
 * SpotBugs and SpotBugs Security. See `build/reports/spotbugs.html`
 * OWASP Dependency Check. Exclusions that are build related are in `etc/suppression.html`. If you need to clobber a
   dependency globally, see the root `build.gradle` file and search for "security vulnerabilities".
 * License Formatting. Should include the header from `etc/HEADER`. Run `./gradlew licenseFormat` to apply, or use the
   default git pre-commit hook.
 * Spotless Java/Spotless Kotlin Check. Run `./gradlew spotlessApply` to apply, or use the default git pre-commit hook.
 * PMD with the following rulesets:
    * [best practices](https://pmd.github.io/latest/pmd_rules_java_bestpractices.html)
    * [error_prone](https://pmd.github.io/latest/pmd_rules_java_errorprone.html) (This will mostly overlap with spotbugs)
    * [security](https://pmd.github.io/latest/pmd_rules_java_errorprone.html)
    * [performance](https://pmd.github.io/latest/pmd_rules_java_performance.html)
 
 NB: If you add a new Java/Kotlin project to the tree, please be sure to add it to the `java_kotlin_projects` list in 
 the root `build.gradle` file so the standard checks will be applied to it.
    
You can omit a class or method by using the ``@NoCoverageGenerated(justification = "Something")`` annotation. Similarly,
you can omit a class or method from SpotBugs using ``@SuppressFBWarnings("code", justification = "Something")``. You
can also suppress a PMD warning using ``@SuppressWarnings("PMD.RuleName")``

You should have a good reason for these. 

You can find reports on each of these in the ``build/reports`` directory of each individual module.