package com.knapsack.repository;

import com.knapsack.model.KnapSackUser;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserRepository extends Repository<KnapSackUser, Long> {

    KnapSackUser save(KnapSackUser manager);

    KnapSackUser findByName(String name);
}
