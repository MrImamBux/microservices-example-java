package de.etecture.microservices.licensingservice.repository;

import de.etecture.microservices.licensingservice.model.Organization;

public interface OrganizationRedisRepository {
    void save(Organization organization);
    void update(Organization organization);
    void delete(String organizationId);
    Organization find(String organizationId);
}
