#!/bin/sh

echo "-----------------------------------------------------------"
echo "Waiting for Discovery Server to start on port $DISCOVERY_SERVICE_PORT"
echo "-----------------------------------------------------------"
while ! nc -z discovery-service $DISCOVERY_SERVICE_PORT; do sleep 3; done
echo "Discovery Service has started"

echo "-----------------------------------------------------------"
echo "Starting Security Service"
echo "-----------------------------------------------------------"
java -Deureka.client.serviceUrl.defaultZone=$DISCOVERY_SERVICE_URI \
    -jar /usr/local/security-service/@ARTIFACT@