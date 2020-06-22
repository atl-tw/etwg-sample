Infrastructure Design
=====================

One thing we really want to stress with this "default" infrastructure is "secure by"
default. In this case we are using a VPC to host our application with a reasonable 
set of defaults.


Network Topology
----------------

[diagram](./infrastructure-physical.puml)

![diagram](http://www.plantuml.com/plantuml/png/bP51JiGm34NtEOKlq0Ktc1DmXSX9FADeNBTod5O8zUuCBIqqW10xo__PF_6_54ErshEBFDG4MOnKo9nID9GR4-1SuDK1m2PUCHBWcFqDZMPyMPLjU1hZ1tcc7ttFMUwRTE1Z3PoZBpK8sUxnVVeXpeFUFG-OV416YQG7kxHBDKzezClMX8O12ns5ZroBaPwuXirIxDtq-M_aoUrCyxZbKv0X4J3s8Q7NAfPxQjnHBLyn6XJIAKVo6ztyUN6BStjUq7NTAPKlzQeT6Up5sltvjcVR_u7jOTs59DMUtm00)

Within the VPC we have two pairs of subnets: public and private. The public we use
for services that need to be externally accessible. That is, in traditional networking
parlance, it is the DMZ for the VPC. Because we want to use the Amazon API Gateway to 
proxy our front end we have a VpcLink that connects to a network load balancer inside our
VPC. This allows API Gateway to reach services homed on the public network without our VPC. 

Additionally, each of the subnets is split into an "a" and "b" component, so they can be spread
across availability zones within the region that hosts our VPC.

Understanding Where to Put Things
---------------------------------

If you are deploying something that should be accessible outside of the VPC, you should attach it to
the public subnets. If your service or component only talks to private resources, you can put it in the private subnets.
The point here is to maintain a clean isolation between things that are intended to be public, and those that aren't.
You should always assume that anything that is reachable by the user could potentially be compromised with the full 
access privileges of the component. If you minimize the physical access (in addition to RBAC/IAM/Mesh access), you 
can further impede an attacker that penetrates a particular component.
 