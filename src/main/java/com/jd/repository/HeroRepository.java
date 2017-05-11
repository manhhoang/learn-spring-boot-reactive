package com.jd.repository;

import com.jd.models.Hero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends CrudRepository<Hero, Long> {

	public Hero findByName(String name);
}
