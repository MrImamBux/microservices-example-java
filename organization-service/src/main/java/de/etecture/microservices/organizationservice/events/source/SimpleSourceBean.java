package de.etecture.microservices.organizationservice.events.source;

import de.etecture.microservices.organizationservice.events.models.OrganizationChangeModel;
import de.etecture.microservices.organizationservice.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {
    private Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    /**
     * With Constructor Injection, Spring Cloud Stream will inject a Source interface
     * implementation for use by the service.
     * @param source
     */
    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishOrgChange(String action, String organizationId) {
        logger.debug("Sending Kafka message {} for organization id: {}", action, organizationId);

        OrganizationChangeModel change = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(),
                action,
                organizationId,
                UserContextHolder.getContext().getCorrelationId());

        source.output()
                .send(MessageBuilder
                        .withPayload(change)
                        .build()
                );
    }


}
