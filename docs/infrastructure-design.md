Infrastructure Design
=====================

One thing we really want to stress with this "default" infrastructure is "secure by"
default. In this case we are using a VPC to host our application with a reasonable 
set of defaults.


Network Topology
----------------

[diagram](./infrastructure-physical.puml)

![diagram](http://www.plantuml.com/plantuml/png/bP11ReGm34Ntd28Nu0vpa-WdyLJMc80SXr5LzUwDCWm5bKtPy6K_lyY-5ODQdMI8CjVaE1jf9Xjwk2EImyB-0qPFV6xGkaJtDRZs_LAjveNpevTVs5A3SFIbXYOisyHvgZMUipwyp4W-G90ZQUU-ZoBb5KONfWH3GA4z-2YrsExelpmdSe8f97YjsNYYWm9gV4SqLqXNZkHVv2PeYlT9t3YEfsCSSk_j9tY7NhrUyVpOly2_kzuefph93m00)

Within the VPC we have two pairs of subnets: public and private. The public we use
for services that need to be externally accessible. That is, in traditional networking
parlance, it is the DMZ for the VPC. Because we want to use the Amazon API Gateway to 
proxy our front end we have a VpcLink that connects to a network load balancer inside our
VPC. This allows API Gateway to reach services homed on the public network without our VPC. 

Additionally, each of the subnets is split into an "a" and "b" component so they can be spread
across availability zones within the region that hosts our VPC.

Understanding Where to Put Things
---------------------------------

