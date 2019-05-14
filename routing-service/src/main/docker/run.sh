#!/bin/sh

echo "-----------------------------------------------------------"
echo "Waiting for Discovery Server to start on port $DISCOVERY_SERVICE_PORT"
echo "-----------------------------------------------------------"
while ! nc -z discovery-service $DISCOVERY_SERVICE_PORT; do sleep 3; done
echo "Discovery Service has started"

echo "-----------------------------------------------------------"
echo "Waiting for Configuration Server to start on port $CONFIGURATION_SERVER_PORT"
echo "-----------------------------------------------------------"
while ! nc -z configuration-server $CONFIGURATION_SERVER_PORT; do sleep 3; done
echo "Configuration Server has started"

echo "-----------------------------------------------------------"
echo "Waiting for Logging GUI Service to start on port $LOGGING_GUI_PORT"
echo "-----------------------------------------------------------"
while ! nc -z logging-gui $LOGGING_GUI_PORT; do sleep 3; done
echo "Logging GUI service has started"

echo "-----------------------------------------------------------"
echo "Starting Routing Service"
echo "-----------------------------------------------------------"
java -Deureka.client.serviceUrl.defaultZone=$DISCOVERY_SERVICE_URI \
     -Dspring.cloud.config.uri=$CONFIGURATION_SERVER_URI \
     -Dspring.zipkin.baseUrl=$LOGGING_GUI_URI \
     -jar /usr/local/routing-service/@ARTIFACT@