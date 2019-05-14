FROM openjdk:8-jre-alpine

ARG DISCOVERY_SERVICE_URI
ARG DISCOVERY_SERVICE_PORT

# sets environment variable
ENV DISCOVERY_SERVICE_URI ${DISCOVERY_SERVICE_URI}
ENV DISCOVERY_SERVICE_PORT ${DISCOVERY_SERVICE_PORT}

RUN mkdir /usr/local/security-service

# copy resources from local to docker
ADD @ARTIFACT@ /usr/local/security-service
ADD run.sh run.sh

# provide execute permissions and execute the scripts
RUN chmod +x run.sh
CMD ./run.sh