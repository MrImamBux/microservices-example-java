package de.etecture.microservices.licensingservice.repository;

import de.etecture.microservices.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class OrganizationRedisRepositoryImpl implements OrganizationRedisRepository {

    // name of the hash in redis server where organization data is stored
    private static final String HASH_NAME = "organization";

    private RedisTemplate<String, Organization> redisTemplate;

    // set of Spring helper methods for carrying out data operations on the redis server
    private HashOperations hashOperations;

    public OrganizationRedisRepositoryImpl() {
        super();
    }

    @Autowired
    private OrganizationRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(Organization organization) {
        hashOperations.put(HASH_NAME, organization.getOrganizationId(), organization);
    }

    @Override
    public void update(Organization organization) {
        hashOperations.put(HASH_NAME, organization.getOrganizationId(), organization);
    }

    @Override
    public void delete(String organizationId) {
        hashOperations.delete(HASH_NAME, organizationId);
    }

    @Override
    public Organization find(String organizationId) {
        return (Organization) hashOperations.get(HASH_NAME, organizationId);
    }
}
