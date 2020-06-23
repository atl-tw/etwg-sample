Things that should be done to this:

Lambda-Service
 * Change the Repository to use the Pageable/sortable repo and add paging to the Thing Result
 * Spring Actuator setup
 * Fill out swagger annotations
 * Shade out tomcat libs.
 
Lambda-Service-Model 
 * Bean Validation
 
 
 Large Project Changes
 * package the deployment for each component from the component's directory
   and publish as a zip. The infrastructure project should then assemble these
   into the deployment group.
   
   
Pipeline
 * The pipeline module is still mostly blank, but it should have code to create projects for 
   each of the modules with a local jenkins file.