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
 
You will also get a git `pre-commit` hook that formats source files and applies 
the license header located in `./etc/HEADER`


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

 * JaCoCo Test Coverage parameters. See the root `build.gradle` and search for "jacocoTestCoverageVerification".
 * SpotBugs and SpotBugs Security. See `build/reports/spotbugs.html`
 * OWASP Dependency Check. Exclusions that are build related are in `etc/suppression.html`. If you need to clobber a
   dependency globally, see the root `build.gradle` file and search for "security vulnerabilities".
 * License Formatting. Should include the header from `etc/HEADER`. Run `./gradlew licenseFormat` to apply, or use the
   default git pre-commit hook.
 * Spotless Java/Spotless Kotlin Check. Run `./gradlew spotlessApply` to apply, or use the default git pre-commit hook.
 
 NB: If you add a new Java/Kotlin project to the tree, please be sure to add it to the `java_kotlin_projects` list in 
 the root `build.gradle` file so the standard checks will be applied to it.
    