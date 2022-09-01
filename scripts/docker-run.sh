docker run -it --rm -v /home/core/gatling/conf:/opt/gatling/conf \
-v /home/core/gatling/user-files:/opt/gatling/user-files \
-v /home/core/gatling/results:/opt/gatling/results \
shashikant86/docker-gatling mvn clean gatling:test -Dgatling.simulationClass=UserContextSimulation
