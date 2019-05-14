FROM openjdk:8-jre-alpine

RUN mkdir /usr/local/discovery-service

# copy resources from local to docker
ADD @ARTIFACT@ /usr/local/discovery-service
ADD ./run.sh run.sh

# provide execute permissions and execute the scripts
RUN chmod +x run.sh
CMD ./run.sh