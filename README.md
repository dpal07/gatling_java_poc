# Performance POC using Gatling Java Maven
 This is to showcase how Gatling using Java Maven can be used for Performance testing. 


## Env setup:

### JDK installation [Java version should not be lower than 8.0] 

- Make sure you have Homebrew installed. if not run the following command from your bash/terminal:
  - /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
- Install JDK
  - brew install openjdk@<version> //chosen version or 
  - brew install openjdk //latest version
-Setup JAVA_HOME environment variable:
  - export JAVA_HOME=<path-to-jdk>
    export PATH=$JAVA_HOME/bin:$PATH

### Install maven (version > 3.0.0)

- Install maven
  - brew install maven
- Setup MAVEN_HOME environment variable:
  - export MAVEN_HOME=<path-to-maven>
    export PATH=$MAVEN_HOME/bin:$PATH
  
### Get the sample test

- Clone the project to your Local.

### Adding a test/ Simulation

- It's a simple java class with a simulation. The simulations can be called either from the simulation class constructor. While uploading the package to the Gatling cloud that time deserving simulation class can be mentioned in the commandline. 

### Executing tests 

- 'mvn gatling:enterprisePackage'  :: command to create the package. 
- 'mvn gatling:enterpriseStart'  :: command to upload the package to gatling enterprise cloud. 
  - The command will prompt for the below input
    -  Do you want to create a new simulation or start an existing one?
    - Choose one simulation class in your package: [Enter your simulaton class name]
    -  Enter a simulation name : [The simulation will display on the Gatling Cloud. The same class can have different simulations]
    -  Choose the load injectors region :[select the load region]
- Run 'sh scripts/run-sqs-script.sh' to run the sqs test script. 
- Executing tests in local docker container:
  - Run, sh scripts/run_local.sh, this will create the local docker container and then run the tests inside the container. 
- Executing a test using maven command :
  - mvn clean gatling:test -Dgatling.simulationClass=<simulation class name>

 
 ### Note: 
  - the sample test script already have specified the duration of the tests and the number of constant virtual users. But these can be overwritten while generating the package and uploading to Gatling Cloud. 
  - Existing simulations can be found here: https://cloud.gatling.io/o/gp/simulations

 ### Reference documents:
 - https://gatling.io/docs/gatling/reference/current/http/request/
 - https://gatling.io/docs/gatling/reference/current/extensions/maven_plugin/




