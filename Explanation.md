#Functional Requirements

## Core
* Variables
  * Map of <ServiceName, Service URL>
    * Later changed to discovery using broadcast message
* Functions
  * getPropertyMap(String serviceName) -> Empty Property Map
  * run(String serviceName, JSON propertyMap) -> JSON Result


## MPS (Property Service)
* Varaibles
  * Map of <Name, JSONProperties>
* Functions
  * store(String name, JSON Properties) -> Boolean
  * retrieve(String name) -> JSON Properties

## Tooling Service
* Functions
  * run(String name) -> JSON Result

## WebApp
* PRTA button is clicked
* Calls Core/getPropertyMap(PRTA) 
* Generates a web form based off the PTEF properties JSON template
* User populates this template
* Populated properties are sent to core/run(Properties)
* Core uploads those properties to MPS and sends the name to PTEF to run
* PTEF asks MPS for properties for this run and finishes its run
* PTEF sends report back to Core, Core sends results back to WebApp
