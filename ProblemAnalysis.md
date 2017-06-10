# Problem Analysis

## High level Goals (contest specifications)
* More user friendly 
* Easier for tooling to access common properties
* Elegant


## Unrealized Problems
* Tools unable to communicate with each other

* Incredibly difficult and complex learning curve to reach 1 successful run of any tool
  * Too many tutorials to follow, too many settings to change just to run a simple test

* Lack of support for anything other than Java 
  * Restricting possible approaches to a new problem

* High overhead during the initial setup phases for every tool
  * Only a few people know how to set everything up
  * Leads to many environment problems that should never occur for a user

## The Solution
### Microservices

* Everything is a microservice
  * Tools handle one specific task, very efficently

* Tools are setup on central machines and run through an interface (web/anything)
  * This means one time setup and maintainence, no more setup issues

* Inter-tool communication is done through HTTP
  * HTTP is easily accessible from any platform and any programming language
  * Using a series of HTTP methods (GET, PUT, POST, DELETE...) anything can use a tool

* Every tool will have a set of API endpoints that clearly define that tool's usage.

