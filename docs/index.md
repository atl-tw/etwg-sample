ETWG Example Project
====================

Purpose
-------

This project is here to demonstrate architectural concerns on a simple cloud application. It is important to understand
that the TECHNOLOGIES involved here are not as important as the ideas. To that end, we have tried to keep the examples
as lean as possible for this first version. It is our hope that you can pick and choose elements of this to use as 
a project template, even if you don't adopt it wholesale.

Getting Started
---------------

The first thing I would want you to note is the [README](../README.md) file. You can see that the project setup
is a single line that then downloads and configures ALL The components necessary for the project and configures them
to run within the scope of the project directory.

This is important for a number of reasons:

 1. It makes onboarding a new team member easy. The prerequisites for getting the project building and deploying are
    minimal. If you have a "project setup" document that is more than 20 lines long for new team members to follow 
    (not including account setup), you are doing it wrong. This will download the whole of the internet, but will ensure 
    that all requirement to build and deploy the project are met.
 1. It makes working with your CI/CD environment MUCH easier. You will definitely want to cache your .gradle directory
    between builds, but you should have almost no dependencies on "configured" build environments. This will make your
    life easier if you are working with an external group that manages the build server, or a cloud-based build server.
 1. Versions of all the things are known-values across all the environments. Your builds should be perfectly repeatable.    
    
You don't have to use Gradle to manage this, but you SHOULD have some kind of script that can be run that configures
all the things that are needed for the user.

Project Design Considerations
-----------------------------

A number of opinions have been baked into this.

### Mono-Repo style, independent executions

Here we have made some technology decisions: we are using Gradle to manage the entire processes; we are writing our 
code in Java and Kotlin; we are deploying our code with terraform into an Amazon VPC using Amazon-native services.

All of these decisions are flexible. You could just as easily have chosen to use yarn/lerna, pnpm, or make. You could
be deploying with Amazon CDK or Helm charts or any combination of the above. The important thing here is to see WHY the
components work the way they do, much more than the how.

 1. All the code is as close together as possible. If a bit of code needs access to a new resource, then the changes
    to the infrastructure can be made as part of the same changeset.
 1. There is a single, self-consistent design to how all the pieces work.
 1. Effort has been takes to not include new  tooling where there is existing tooling that does the job. 
    This gives the user a single point of entry into the 
    project -- the ``gradlew`` script, and it should be easy to follow from there. If you are using yarn as your primary
    build tool, then everything should go through yarn. Don't make the user hunt down scripts that are scattered through
    the code to configure their local dev environment or the cloud environment. Understand that even something as 
    seemingly mundane as BASH scripts can include a lot of assumptions about what OS and tools are available on
    the machine you are using. Either ensure these tools are installed to the local file structure, or favor tools like
    gradle or yarn that give to the flexibility to do all you need to do with a single larger tool. This can be even 
    more important if the project sits on a shelf for a while and the default toolchains installed on workstations or
    build servers change.
 1. Each major component should have its own ``README`` file that clarifies the purpose of the component -- giving the
    reader hits of what to put in there vs what NOT to put in there -- and an overview of the general structural design
    of the component. These should be generally self-similar down your project hierarchy.
 1. The infrastructure project knows how to deal with our versioning strategy, and can deploy assets from whatever the
    appropriate repository is using named versions. That is, it is independent of the local code build for what it will
    deploy for a particular run.
 1. Finally, the structure of the project is as evergreen as it can be made. You should be able to maintain the general
    structure of the project even if the underlying tooling around it changes.
    

### Phoenix versions of the deployment

While we have two "environments", you can do parallel deployments of the entire suite within each environment using 
named "versions". Typically, as you build and publish individual components, you will want to version them. Here we 
are looking to a "BUILD_NUMBER" to increment the least significant version number, but for users working from their 
local machine, we fall back to the user name. The user can then deploy an entire version of all the components as with
an "environment-version" combination of "dev-username" for testing _in situ_. But more importantly, as components are
deployed, a master version containing all the versions of all the components is maintained, and when you go to deploy
and migrate to a new environment (read: prod), you are deploying a known set of bits across all the artifacts, whether
they be JAR files or Docker images or ZIP files stored... somewhere.

The infrastructure project then assembles all the named versions and deploys them with their respective version numbers
to the requested environment. This means the code can be deployed and smoke tested in the environment while "dark".

Next, the "migration" module switches the API gateway deployment for the environment
to the latest version of the endpoints, and the code is live.

Finally, at some point after the code is live and everyone is satisfied with it, the previous version of all the 
components can be destroyed.

There are any number of advantages to this deployment model, but zero-downtime and graceful fail-backs are the ones 
most likely to sell it. Just as importantly, if you have large suites of integration/smoke tests, especially across
multiple client platforms (web, ios, android), or you wish to do meaningful performance testing with production resource
configuration, this allows you to spin up and tear down environments that might be expensive to maintain.

#### Caveats

It is important to understand for the migration process to work, there are certain infrastructure elements that *shouldn't*
change with each version -- specifically AuthN services, Gateways, and Storage elements. This does mean it is possible 
for a new version to fundamentally damage previous versions that are still running. Care must be taken when making
alterations to these elements that no harm is done.


For more information about the how and why of the ``infrastructure`` module design, see the 
[Terraform Practice](./terraform-practice.md) and [physical infrastructure](./infrastructure-design.md) documents.

### Secure by default infrastructure and development

Particularly when developing with AWS resources, it is easy to fall into a pattern of not securing resources to the 
level that they *can* be secured. With this sample project we are demonstrating a model for security that starts with 
the development practice (managing explicit security for each component) down to the infrastructure level (ensuring 
the network topology physically isolates resources in a reasonable manner).


Code Governance and Fitness Functions
-------------------------------------

It is important to *start* your project with code governance in place. You can always change it later if you want to, 
but starting with some reasonable defaults makes it easier to make sure you never end up in a bad place. You can see
the "Project Quality Standards" section of the main [README](../README.md) for information.

 > What about SonarQube? Or Fortify? Or AppScan?

These are all perfectly OK tools (well, maybe not Fortify). Largely, they do EXACTLY the kind of quality metrics we have 
implemented in our build process. The difference is the feedback cycle: developers can perform all of these quality 
metrics at their desk and ensure that when they push they aren't going to surprise break the build.

The other problem is all of these (self/cloud) hosted tools are FLAKY and/or SLOW. Keeping them out of your tight build
loop will save you a world of pain, and you can deal with them when you look to deploy if your organization requires 
them. 


Final Thoughts
--------------

This project is meant as a starting point, but with a number of patterns that we hope you can maintain no matter the 
project or underlying technology.

 1. Even within the scope of your "Infrastructure as Code" code, still truly separate infrastructure from things that 
    might change frequently from deployment to deployment.
 1. Try to make sure it is possible to deploy ad-hoc and phoenix versions of your application to your runtime environment. 
    Largely this can be accomplished by just sticking to a good set of naming conventions for resources inside your 
    cloud or k8s environment. Remember, the next deployment shouldn't replace the current, but should run along side it.
 1. Think about security in the most expansive way possible at the beginning of a project, then grant permissions as you 
    go. This is far and away the easiest way to implement the "Principle of Least Access". If you start with a wide open
    system and then have to retrofit it with security concerns it will take seemingly forever.
 1. If you don't start with this project, please, start with a project that has as much code governance/fitness function
    evaluations in place as possible. Much like security, you can always relax the rules, but if you end up with even a
    few thousand lines of code, addressing them after the fact can become a monumental task.
    






