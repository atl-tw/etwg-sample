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

The first thing I woudl want you to note is the [README](../README.md) file. You can see that the project setup
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

 1. All of the code is as close together as possible. If a bit of code needs access to a new resource, then the changes
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
[Terraform Practice](./terraform-practice.md) and [physical infrastructure](./infrastructure-design.md) documentations.

### Secure by default infrastructure and development

Particularly when developing with AWS resources, it is easy to fall into a pattern of not securing resources to the 
level that they *can* be secured. With this sample project we are demonstrating a model for security that starts with 
the development practice (managing explicit security for each component) down to the infrastructure level (ensuring 
the network topology physically isolates resources in a reasonable manner).




Code Governance and Fitness Functions
-------------------------------------



Final Thoughts
--------------








