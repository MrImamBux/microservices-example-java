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
echo "Waiting for Security Service to start on port $SECURITY_SERVICE_PORT"
echo "-----------------------------------------------------------"
while ! nc -z security-service $SECURITY_SERVICE_PORT; do sleep 3; done
echo "Security Service has started"

echo "-----------------------------------------------------------"
echo "Waiting for Database Server to start on port $DATABASE_SERVER_PORT"
echo "-----------------------------------------------------------"
while ! nc -z database $DATABASE_SERVER_PORT; do sleep 3; done
echo "Database Server has started"

echo "-----------------------------------------------------------"
echo "Waiting for Cache Server to start on port $CACHE_SERVER_PORT"
echo "-----------------------------------------------------------"
while ! nc -z cache-server $CACHE_SERVER_PORT; do sleep 3; done
echo "Cache Server has started"

echo "-----------------------------------------------------------"
echo "Waiting for Logging GUI Service to start on port $LOGGING_GUI_PORT"
echo "-----------------------------------------------------------"
while ! nc -z logging-gui $LOGGING_GUI_PORT; do sleep 3; done
echo "Logging GUI service has started"

echo "-----------------------------------------------------------"
echo "Waiting for ZooKeeper Server to start on port $ZOOKEEPER_PORT"
echo "-----------------------------------------------------------"
while ! nc -z zookeeper $ZOOKEEPER_PORT; do sleep 3; done
echo "ZooKeeper server has started"

echo "-----------------------------------------------------------"
echo "Waiting for Kafka Server to start on port $KAFKA_PORT"
echo "-----------------------------------------------------------"
while ! nc -z kafka $KAFKA_PORT; do sleep 3; done
echo "Kafka server has started"

echo "------------------------------------------------------------------------------------------------------------------"
echo "Starting Licensing Service with Configuration Service via Discovery Service: $DISCOVERY_SERVICE_URI on port $DISCOVERY_SERVICE_PORT"
echo "------------------------------------------------------------------------------------------------------------------"
java -Dspring.profiles.active=$PROFILE \
     -Dserver.port=$SERVER_PORT \
     -Deureka.client.serviceUrl.defaultZone=$DISCOVERY_SERVICE_URI \
     -Dspring.cloud.config.uri=$CONFIGURATION_SERVER_URI \
     -Dsecurity.oauth2.resource.userInfoUri=$SECURITY_SERVICE_URI \
     -Dspring.datasource.url=$DATABASE_SERVER_URI \
     -Dspring.redis.host=$CACHE_SERVER_URI \
     -Dspring.zipkin.baseUrl=$LOGGING_GUI_URI \
     -Dspring.cloud.stream.bindings.binder.zkNodes=$ZOOKEEPER_URI \
     -Dspring.cloud.stream.bindings.binder.brokers=$KAFKA_URI \
     -Dspring.cloud.stream.bindings.binder.brokers=$KAFKA_URI \
     -Dspring.cloud.stream.kafka.binder.brokers=$KAFKA_URI \
     -jar /usr/local/licensing-service/@ARTIFACT@