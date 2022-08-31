#!/bin/bash
CLASS=$1
CONTAINER_NAME="docker-gatling-container"
IMAGE_NAME="shashikant86/docker-gatling"


check_image_exist() {
  echo -e "\n*** Checking if docker image exists for the web-scraper... ***\n"

  if docker images | grep -w ${IMAGE_NAME}
  then
  echo -e "\n*** Image already exists. We can run container... ***\n"

  else
  build_image
  fi
}

delete_old_reports() {
rm -rf results/
docker exec $CONTAINER_NAME rm -rf /opt/gatling/results/*
}

build_image() {

  echo -e "\n*** Building the image ***\n"
   docker build -t ${IMAGE_NAME} .
   echo -e "\n*** Finished building the image ***\n"

}

check_container_exist() {

   echo -e "\n *** Deleting old unused containers"

   docker rm $(docker ps -a | grep $CONTAINER_NAME | awk '{print $3}')

  echo -e "\n*** Checking if the container exists ***\n"

    if docker ps -a | grep ${CONTAINER_NAME}
    then
        echo -e "\n*** Container already exists ***\n"
        docker start ${CONTAINER_NAME}
    else
        echo -e "\n*** Running the container ***\n"
        start_container_with_Gatling
    fi
}

start_container_with_Gatling() {
  docker run -it -d --rm -v conf:/opt/gatling/conf \
  -v user-files:/opt/gatling/user-files \
  -v results:/opt/gatling/results \
  --name $CONTAINER_NAME $IMAGE_NAME
}

stop_container() {
docker stop ${CONTAINER_NAME}
}

run_gatling_test() {
docker exec ${CONTAINER_NAME} /opt/gatling/bin/gatling.sh -sf /opt/gatling/src/test/java/ -s UserContextSimulation -rf /opt/gatling/results/
}



check_image_exist
check_container_exist
delete_old_reports
start_container_with_Gatling
run_gatling_test
stop_container