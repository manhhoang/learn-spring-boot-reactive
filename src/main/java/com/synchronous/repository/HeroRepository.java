package com.synchronous.repository;

import com.synchronous.models.Hero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends CrudRepository<Hero, Long> {

	public Hero findByName(String name);
}
