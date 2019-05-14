package de.etecture.microservices.licensingservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import de.etecture.microservices.licensingservice.clients.OrganizationRestTemplateClient;
import de.etecture.microservices.licensingservice.model.License;
import de.etecture.microservices.licensingservice.model.Organization;
import de.etecture.microservices.licensingservice.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    OrganizationRestTemplateClient organizationRestTemplateClient;

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "50000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
    )
    public License getLicense(String organizationId, String id) {
        License license = licenseRepository.findByOrganizationIdAndId(organizationId, id);

        Organization organization = organizationRestTemplateClient.getOrganization(organizationId);

        return license
                .withOrganizationName(organization.getName())
                .withContactName(organization.getContactName())
                .withContactEmail(organization.getContactEmail())
                .withContactPhone(organization.getContactPhone());
    }

    @HystrixCommand(
            fallbackMethod = "buildFallbackLicenseList", // defines a single function in your class that will be called if call from Hystrix fails
            threadPoolKey = "LicensesByOrganizationThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10"),
            },
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "50000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
    )
    public List<License> getLicensesByOrganization(String organizationId) {
        List<License> licenses = licenseRepository.findByOrganizationId(organizationId);

        Organization organization = organizationRestTemplateClient.getOrganization(organizationId);

        for (License license : licenses) {
            license
                .withOrganizationName(organization.getName())
                .withContactName(organization.getContactName())
                .withContactEmail(organization.getContactEmail())
                .withContactPhone(organization.getContactPhone());
        }

        return licenses;
    }

    /**
     * In the case of fallback, you return a hard-coded list of License
     * @param organizationId
     * @return
     */
    private List<License> buildFallbackLicenseList(String organizationId) {
        List<License> licenses = new ArrayList<>();
        licenses.add(new License()
                .withId("000000-000000-000000-000000")
                .withOrganizationId(organizationId)
                .withProductName("Sorry no licensing information currently available")
        );

        return licenses;
    }

    public void saveLicense(License license) {
        license.withId(UUID.randomUUID().toString());

        licenseRepository.save(license);
    }

    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.delete(license);
    }
}
