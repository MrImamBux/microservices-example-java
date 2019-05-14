package de.etecture.microservices.licensingservice.clients;

import de.etecture.microservices.licensingservice.model.Organization;
import de.etecture.microservices.licensingservice.repository.OrganizationRedisRepository;
import de.etecture.microservices.licensingservice.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class OrganizationRestTemplateClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrganizationRedisRepository organizationRedisRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    private Organization checkRedisCache(String organizationId) {
        try {
            return organizationRedisRepository.find(organizationId);
        } catch (Exception ex) {
            logger.error("Error encountered while trying to retrieve organization {} check Redis cache. Exception {}", organizationId, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            organizationRedisRepository.save(organization);
        } catch (Exception ex) {
            logger.error("Unable to cache organization {} in Redis. Exception {}", organization.getOrganizationId(), ex);
        }
    }

    public Organization getOrganization(String organizationId) {
        logger.debug("In Licensing Service.getOrganization. Correlation Id {}. ", UserContextHolder.getContext().getCorrelationId());

        Organization organization = checkRedisCache(organizationId);

        if (Objects.nonNull(organization)) {
            logger.debug("Successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }

        logger.debug("Unable to locate organization from the redis cache: {}", organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://routing-service/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);
        organization = restExchange.getBody();

        // saving the retrieved object to the cache
        if (Objects.nonNull(organization)) {
            cacheOrganizationObject(organization);
        }

        return organization;
    }

}
