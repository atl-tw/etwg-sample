Terraform Practice and Design
=============================

Here we are deviating from what most people would consider to be "standard" project configuration with Terraform.  While
working with Equifax, the Thoughtworks team used a slightly different project structure where configuration was managed
by, frankly, and INSANELY large and complex BASH script. While we liked the structure this gave us, the implementation
left a lot to be desired.

To that end, after the end of the project we developed the 
["world-engine" Gradle plugin](https://github.com/atl-tw/world-engine). This encapsulated =the working theory of what
we have developed, but was much cleaner and A LOT more cross platform.

Differences from What You Might Be Used To
------------------------------------------


 