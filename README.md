# WaCoDiS Metadata Connector
![Java CI](https://github.com/WaCoDiS/metadata-connector/workflows/Java%20CI/badge.svg)
  
The WaCoDiS Metadata Connector component provides routines for handling DataEnvelope creation events in order to connect 
to the DataAccess API for creating new metadata resources.

**Table of Content**  
1. [WaCoDiS Project Information](#wacodis-project-information)
2. [Overview](#overview) 
3. [Installation / Building Information](#installation--building-information)
4. [Deployment](#deployment)
5. [User Guide](#user-guide)
6. [Developer Information](#developer-information)
7. [Contact](#contact)
8. [Credits and Contributing Organizations](#credits-and-contributing-organizations)

## WaCoDiS Project Information
<p align="center">
  <img src="https://raw.githubusercontent.com/WaCoDiS/apis-and-workflows/master/misc/logos/wacodis.png" width="200">
</p>
Climate changes and the ongoing intensification of agriculture effect in increased material inputs in watercourses and dams.
Thus, water industry associations, suppliers and municipalities face new challenges. To ensure an efficient and environmentally
friendly water supply for the future, adjustments on changing conditions are necessary. Hence, the research project WaCoDiS
aims to geo-locate and quantify material outputs from agricultural areas and to optimize models for sediment and material
inputs (nutrient contamination) into watercourses and dams. Therefore, approaches for combining heterogeneous data sources,
existing interoperable web based information systems and innovative domain oriented models will be explored.

### Architecture Overview

For a detailed overview about the WaCoDiS system architecture please visit the 
**[WaCoDiS Core Engine](https://github.com/WaCoDiS/core-engine)** repository.  

## Overview  
The WaCoDiS Metadata Connector is an intermediate component with the scope to handle asynchronous DataEnvelope creation
events in order to interact with the DataAccess API for creating new metadata resources. Therefore, the Metadata Connector
subscribes for DataEnvelope creation messages at a message broker which are published by the 
**[Datasource Observer](https://github.com/WaCoDiS/datasource-observer)** if new required datasets are found. Following,
the Metadata Connector checks whether metadata for the found datasets have been already registered at the 
**[Data Access API](https://github.com/WaCoDiS/data-access-api)** or not and creates or updates corresponding metadata
information.
### Modules
The WaCoDiS Metadata Connector comprises three Maven modules:
* __WaCoDiS Metadata Connector Models__  
This module contains Java classes that reflect the basic data model. This includes the data types specified with OpenAPI
in the WaCoDiS apis-and workflows repository. All model classes were generated by the OpenAPI Generator, which is integrated
and can be used as Maven Plugin within this module.  
* __WaCoDiS Metadata Listener__  
This module is responsible for the message broker subscription as well as the interconnection to the Data Access API.
In addition, for each DataEnvelope type it contains a separate metadata update strategy.  
* __WaCoDiS Metadata Connector App__ 
Since WaCoDiS Metadata Connector is implemented as Spring Boot application, the App module provides the application runner
as well as default externalized configurations.
### Technologies
* __Java__  
WaCoDiS Metadata Connector is tested with Oracle JDK 8 and OpenJDK 8. Unless stated otherwise later Java versions can be used as well.
* __Maven__  
This project uses the build-management tool [Apache Maven](https://maven.apache.org/)
* __Spring Boot__  
WaCoDiS Metadata Connector is a standalone application built with the [Spring Boot](https://spring.io/projects/spring-boot) 
framework. Therefore, it is not necessary to deploy WaCoDiS Metadata Connector manually with a web server.  
* __Spring Cloud__  
[Spring Cloud](https://spring.io/projects/spring-cloud) is used for exploiting some ready-to-use features in order to implement
an event-driven workflow. In particular, [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream) is used
for subscribing to asynchronous messages within the WaCoDiS system.
* __RabbitMQ__  
For communication with other WaCoDiS components of the WaCoDiS system the message broker [RabbitMQ](https://www.rabbitmq.com/)
is utilized. RabbitMQ is not part of WaCoDiS Metadata Connector and therefore [must be deployed separately](#dependencies).
* __OpenAPI__  
[OpenAPI](https://github.com/OAI/OpenAPI-Specification) is used for the specification of data models used within this project.

## Installation / Building Information
### Build from Source
In order to build the WaCoDiS Metadata Connector from source _Java Development Kit_ (JDK) must be available. Data Access 
is tested with Oracle JDK 8 and OpenJDK 8. Unless stated otherwise later JDK versions can be used.  

Since this is a Maven project, [Apache Maven](https://maven.apache.org/) must be available for building it. Then, you
can build the project by running `mvn clean install` from root directory

### Build using Docker
The project contains a Dockerfile for building a Docker image. Simply run `docker build -t wacodis/metadata-connector:latest .`
in order to build the image. You will find some detailed information about running the Metadata Connector as Docker container
within the [deployment section](#run-with-docker).

### Configuration
Configuration is fetched from [WaCoDiS Config Server](https://github.com/WaCoDiS/config-server). If config server is not
available, configuration values located at *src/main/resources/application.yml* within the Metadata Connector App submodule
are applied instead.   
#### Parameters
WaCoDiS Metadata Connector is a Spring Boot application and provides an _application.yml_ within the metadata-connector-app
module for configuration purpose. A documentation for common application properties can be found at
https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html

In addition, some configuration parameters relate to different Spring Cloud components, i.e. [Spring Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/)
and [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/).

To get started the most relevant configuration parameters are described below.

* `spring.rabbitmq.host`: Name of the node running a RabbitMQ instance
* `spring.rabbitmq.port`: RabbitMQ port to connect to
* `spring.rabbitmq.username`: Username for RabbitMQ connections
* `spring.rabbitmq.password`: Password for RabbitMQ connectio
* `spring.cloud.stream.bindings.input-data-envelope.destination`: Topic used to listen for published DataEnvelope messages
* `dataaccess.uri`: URL that points to a WaCoDiS DataAccess API instance

## Deployment
### Dependencies
WaCoDiS Metadata Connector requires a running RabbitMQ instance for consuming messages as well as a running
WaCoDiS DataAccess API for creating or updating metadata resources. For starting a RabbitMQ instance as Docker container
a _docker-compose.yml_ is provided at _./docker/rabbitmq_. Detailed deployment instructions for the DataAccess API can 
be found at https://github.com/WaCoDiS/data-access-api. 

### Run with Maven
Just start the application by running `mvn spring-boot:run` from the root of the `metadata-connector-app` module. Make
sure you have installed all dependencies with `mvn clean install` from the project root.

### Run with Docker
For convenience, a _docker-compose.yml_ is provided for running the Metadata Connector as Docker container. Just, run 
`docker-compose up` from the project root. The latest Docker image will be fetched from [Docker Hub](https://hub.docker.com/repository/docker/wacodis/metadata-connector).
The _docker-compose.yml_ also contains the most important configuration parameters as environment variables. Feel free
to adapt the parameters for your needs.

## User Guide
WaCoDiS Metadata Connector is part of the great microservice-oriented WaCoDiS System with the scope to handle metadata
information about datasets that have been published by [WaCoDiS Datasource Observer](https://github.com/WaCoDiS/datasource-observer).
For this purpose it listens for asynchronous `DataEnvelope` messages and interconnects to [WaCoDiS DataAccess API](https://github.com/WaCoDiS/data-access-api).
Hence, Metadata Connector does not provide an API endpoint that can be requested. However, for development or testing
purposes you can publish `DataEnvelope` messages manually via AMQP. A lightweight AMQP publishing client can be found at
https://github.com/WaCoDiS/Tools.

## Developer Information

### Developer guidelines
Up to now, Metadata Connector supports four different dataset types in shape of implementations of the
`AbstractDataEnvelope` model class 
* `CopernicusDataEnvelope`: Describes a Copernicus resource e.g., provided at CODE-DE or ESA Open Access Hub.
* `DwdDataEnvelope`: Holds metadata about datasets provided by the German Weather Services via WFS.
* `SensorWebDataEnvelope`: Describes a SensorWeb resource.
* `WacodisProductDataEnvelope`: Metadata information about a product generated by a WaCoDiS process.

It is possible to enhance Metadata Connector for handling additional dataset types. To do so, the following steps are 
required:
1. Create a model class for the additional data type by implementing `AbstractDataEnvelope`.  
1.1 In order to be consistent with other WaCoDiS components regarding the support for different data types, we strongly
recommend generating the new model classes from an [OpenAPI definition](https://github.com/WaCoDiS/apis-and-workflows/tree/master/openapi).
If not already done, first define your new _DataEnvelope_ within the OpenAPI document.  
1.2 The [Metadata Connector Models module](metadata-connector-models) provides Maven profiles for automatically
generating model classes from an OpenAPI document. The _generate-models_ profile generates the models from a Maven
artifact you first have to create for the [OpenAPI definition](https://github.com/WaCoDiS/apis-and-workflows/tree/master/openapi)
project. By using the _download-generate-models_ profile there is no need to create the artifact in beforehand, since
the execution of this profile will download the latest OpenAPI definitions and then creates the models on top of it. 
You can trigger the profiles by respectively running `mvn clean compile -Pgenerate-models` and
`mvn clean compile -Pdownload-generate-models`.
  
2. To support the updating of dataset types at the [WaCoDiS DataAccess API](https://github.com/WaCoDiS/data-access-api),
you have to provide a new implementation of the `AbstractMetadataUpdater` class. Note, that it is only required to
implement an updating routine for discovered dataset resources that have been all already registered at DataAccess API
but maybe changed over time at the data source. The creation of new datasets will be handled uniformly for all dataset
types, so that there is no need to implement creation routines for new types.

### How to contribute
Feel free to implement missing features by creating a pull request. For any feature requests or found bugs, we kindly
ask you to create an issue. 

### Branching
The master branch provides sources for stable builds. The develop branch represents the latest (maybe unstable)
state of development.

### License and Third Party Lib POM Plugins
Apache License, Version 2.0

### Contributing Developers
|    Name   |   Organization    |    GitHub    |
| :-------------: |:-------------:| :-----:|
| Sebastian Drost | 52° North GmbH | [SebaDro](https://github.com/SebaDro) |
| Arne Vogt | 52° North GmbH | [arnevogt](https://github.com/arnevogt) |
| Matthes Rieke | 52° North GmbH | [matthesrieke](https://github.com/matthesrieke) |

## Contact
The WaCoDiS project is maintained by [52°North GmbH](https://52north.org/). If you have any questions about this or any
other repository related to WaCoDiS, please contact wacodis-info@52north.org.

## Credits and Contributing Organizations
- Department of Geodesy, Bochum University of Applied Sciences, Bochum
- 52° North Initiative for Geospatial Open Source Software GmbH, Münster
- Wupperverband, Wuppertal
- EFTAS Fernerkundung Technologietransfer GmbH, Münster

The research project WaCoDiS is funded by the BMVI as part of the [mFund programme](https://www.bmvi.de/DE/Themen/Digitales/mFund/Ueberblick/ueberblick.html)  
<p align="center">
  <img src="https://raw.githubusercontent.com/WaCoDiS/apis-and-workflows/master/misc/logos/mfund.jpg" height="100">
  <img src="https://raw.githubusercontent.com/WaCoDiS/apis-and-workflows/master/misc/logos/bmvi.jpg" height="100">
</p>

