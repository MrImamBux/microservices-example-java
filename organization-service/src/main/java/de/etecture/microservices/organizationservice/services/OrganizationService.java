package de.etecture.microservices.organizationservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import de.etecture.microservices.organizationservice.events.source.SimpleSourceBean;
import de.etecture.microservices.organizationservice.model.Organization;
import de.etecture.microservices.organizationservice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    SimpleSourceBean simpleSourceBean;

    public Organization getOrganization(String organizationId) {
        return organizationRepository.findByOrganizationId(organizationId);
    }

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "50000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
    )
    public List<Organization> getOrganizations() {
        Iterator<Organization> iterator = organizationRepository.findAll().iterator();
        List<Organization> organizationList = new ArrayList<>();

        iterator.forEachRemaining(organizationList::add);

        return organizationList;
    }

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "50000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
    )
    public void saveOrganization(Organization organization) {
        organization.setOrganizationId(UUID.randomUUID().toString());

        organizationRepository.save(organization);
        simpleSourceBean.publishOrgChange("SAVE", organization.getOrganizationId());
    }

    public void updateOrganization(Organization organization) {
        organizationRepository.save(organization);
    }

    public void deleteOrganization(String organizationId) {
        organizationRepository.deleteById(organizationId);
        simpleSourceBean.publishOrgChange("DELETE", organizationId);
    }
}
