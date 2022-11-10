#!/bin/bash
echo "Running aws sqs test..."

echo "authenticating to aws ...."
aws sso login
sh scripts/tmp-aws-creds.sh
cat $HOME/.tmp-creds. >> $HOME/.aws/credentials

echo "start running the test ..."
mvn clean gatling:test -Dgatling.simulationClass=PricingServiceSqsTest