Infrastructure
==============

This module contains the deployment information for the project. 

Deployment
----------

Gradle tasks:

  1. ``setup``: Installs build time dependencies (pyenv, python, awscli, and terraform) into the root ``.gradle`` folder
  1. ``initializeStateBucket``: Creates the state bucket needed for terraform 
  1. ``boostrapDev``: Bootstraps the dev environment. See the gradle.properties files for the configuration.
  1. ``environmentDev``: Deploys resources specific to the environment (Database, API Gateway)
  1. ``deploymentDev -Pversion=[version]``: Deploys application components to the dev instance for the named version. 
      If no version is specified, the user's username will be used. 
  1. ``migrationDev  -Pversion=[version]``: Configures the named version to be the 'active' version in the def instance.
  
Layout
------

This project contains four primary deployable components:

 1. ``bootstrap``: This component creates a VPC for the application with a standard set of subnets. This also provisions a
    DynamoDB lock table that will be used for the deployment of the rest of the packages. The intention is the team
    runs this deployment *a single time* at environment creation, and it should almost never change.
 1. ``environment``: This component provisions resources that are specific to an environment as a whole and are independent
    of a version. e.g. Storage (Databases, Buckets), ECS/EKS clusters, etc. This process should be run only if there is a 
    major change to the application and is considered "dangerous", since it might destroy data.
 1. ``deployment``: This deploys the actual bits of a component. This might be ECS services, Lambdas, whatever. You should
    also include all IAM configuration for each service at this level, as these may change fairly frequently, but are
    not going to be destructive.
 1. ``migration``: This changes the exposed active version of the application. This could include things like CNAME 
    records, API Gateway deployments, etc. It is presumed that when you run this with an "environment-version" combination,
    you are reconfiguring resources that are at the "environment" level, but you are configuring them for a "version".
    
Design
------


    
    
