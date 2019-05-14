package de.etecture.microservices.organizationservice.controllers;

import de.etecture.microservices.organizationservice.model.Organization;
import de.etecture.microservices.organizationservice.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/organizations")
public class OrganizationServiceController {

    @Autowired
    OrganizationService organizationService;

    @GetMapping
    public List<Organization> getOrganizations() {
        return organizationService.getOrganizations();
    }

    @GetMapping(value = "/{organizationId}")
    public Organization getOrganization(@PathVariable("organizationId") String  organizationId) {
        return organizationService.getOrganization(organizationId);
    }

    @PutMapping
    public void updateOrganization(@RequestBody Organization organization) {
        organizationService.updateOrganization(organization);
    }

    @PostMapping
    public void saveOrganization(@RequestBody Organization organization) {
        organizationService.saveOrganization(organization);
    }

    @DeleteMapping(value = "/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("organizationId") String  organizationId) {
        organizationService.deleteOrganization(organizationId);
    }
}