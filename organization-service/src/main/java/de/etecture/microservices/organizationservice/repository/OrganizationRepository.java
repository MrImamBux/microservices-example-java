package de.etecture.microservices.organizationservice.repository;

import de.etecture.microservices.organizationservice.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String> {
    Organization findByOrganizationId(String organizationId);
}
