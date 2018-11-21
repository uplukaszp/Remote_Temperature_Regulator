# Remote temperature regulator

This is my implementation of the backend of my diploma thesis. It's responsible for controlling devices capable of changing the ambient temperature.

## Installation

### Requirements
* Any computer with maven installed
* Access to MQTT broker
* Raspberry PI in the same local network with Raspian and Java installed

`$ mvn install` to install on remote Raspbery PI,in project directory on your computer with maven.

## Tech stack
* Java 8
* Spring Boot 2
* Spring Security
* Spring Data
* Lombok
* Maven
* H2 
* Eclipse Paho MQTT client
* Quartz Scheduler

## Related projects:
Frontend to this service [link](https://github.com/uplukaszp/Remote_Temperature_Regulator_Front).
