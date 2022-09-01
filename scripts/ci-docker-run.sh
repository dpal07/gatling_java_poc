#!/bin/bash
docker run -t --rm --name my-maven-project -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.3-jdk-8 mvn clean gatling:test -Dgatling.simulationClass=UserContextSimulation