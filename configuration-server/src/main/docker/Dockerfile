FROM openjdk:8-jre-alpine

ARG DISCOVERY_SERVICE_URI
ARG DISCOVERY_SERVICE_PORT

# sets environment variable
ENV ENCRYPT_KEY @ENCRYPT_KEY@
ENV DISCOVERY_SERVICE_URI ${DISCOVERY_SERVICE_URI}
ENV DISCOVERY_SERVICE_PORT ${DISCOVERY_SERVICE_PORT}

RUN mkdir /usr/local/configuration-server

# add curl
RUN apk update && apk upgrade && apk add curl

# download, unzip and move the JCE jars to jvm to facilitate encryption of the sensitive configuration data
RUN cd /tmp/ && \
    # cURL is not recommended. cURL 7.64 (latest) corrupts file while download.
    wget --header="Cookie: oraclelicense=accept-securebackup-cookie" -O jce_policy-8.zip http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip && \
    unzip jce_policy-8 && \
    rm jce_policy-8.zip && \
    yes | cp -v /tmp/UnlimitedJCEPolicyJDK8/*.jar /usr/lib/jvm/java-1.8-openjdk

# copy resources from local to docker
ADD @ARTIFACT@ /usr/local/configuration-server
ADD ./run.sh run.sh

# provide execute permissions and execute the scripts
RUN chmod +x run.sh
CMD ./run.sh