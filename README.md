# Safetynet-alerts-api
An API  that sends data to the emergency services. For exemple, in case of fire, safetyNet Alerts sends the telephone numbers of people living in the building on fire.
This app was build with Spring boot, uses Java to run and stores the data in MongoDB.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

- Java 1.8
- Maven 3.8.1
- MongoDB 5.0.2

### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:

https://maven.apache.org/install.html

3.Install MongoDB:

https://www.mongodb.com/try/download/community


### Running App

Post installation of MongoDB, Java and Maven, you will have to set up an empty database named "SafetyNet".

Finally, you will be ready to import the code into an IDE of your choice and run the project as Spring Boot App or with the maven command line spring-boot:run.

### Testing

The app has unit tests and integration tests written. 
To run the tests from maven and generate the coverage and tests reports, go to the folder that contains the pom.xml file and execute the below command.

`mvn test`
