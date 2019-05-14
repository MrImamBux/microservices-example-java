package de.etecture.microservices.licensingservice.events.handlers;

import de.etecture.microservices.licensingservice.events.models.OrganizationChangeModel;
import de.etecture.microservices.licensingservice.repository.OrganizationRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class) // use the channels defined in the Sink interface to listen for incoming messages
public class OrganizationChangeHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeHandler.class);

    @Autowired
    OrganizationRedisRepository organizationRedisRepository;

    @StreamListener(Sink.INPUT)
    public void loggerSink(OrganizationChangeModel organizationChangeModel) {
        logger.debug("Received a message of type " + organizationChangeModel.getType());

        switch(organizationChangeModel.getAction()){
            case "GET":
                logger.debug("Received a GET event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                break;
            case "SAVE":
                logger.debug("Received a SAVE event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                break;
            case "UPDATE":
                logger.debug("Received a UPDATE event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                organizationRedisRepository.delete(organizationChangeModel.getOrganizationId());
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the organization service for organization id {}", organizationChangeModel.getOrganizationId());
                organizationRedisRepository.delete(organizationChangeModel.getOrganizationId());
                break;
            default:
                logger.error("Received an UNKNOWN event from the organization service of type {}", organizationChangeModel.getType());
                break;
        }

    }

}
