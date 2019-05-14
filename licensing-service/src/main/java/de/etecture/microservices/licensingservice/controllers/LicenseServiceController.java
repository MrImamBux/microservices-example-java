package de.etecture.microservices.licensingservice.controllers;

import de.etecture.microservices.licensingservice.model.License;
import de.etecture.microservices.licensingservice.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

    @Autowired
    LicenseService licenseService;

    @GetMapping("/")
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
        return licenseService.getLicensesByOrganization(organizationId);
    }

    @GetMapping("/{licenseId}")
    public License getLicenses(@PathVariable("organizationId") String organizationId, @PathVariable("licenseId") String licenseId) {
        return licenseService.getLicense(organizationId, licenseId);
    }

    @PutMapping("/{licenseId}")
    public void updateLicenses(@PathVariable("organizationId") String organizationId, @RequestBody License license) {
        licenseService.updateLicense(license.withOrganizationId(organizationId));
    }

    @PostMapping("/{licenseId}")
    public void saveLicenses(@PathVariable("organizationId") String organizationId, @RequestBody License license) {
        licenseService.saveLicense(license.withOrganizationId(organizationId));
    }

    @DeleteMapping("/{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLicenses(@RequestBody License license) {
        licenseService.deleteLicense(license);
    }
}
