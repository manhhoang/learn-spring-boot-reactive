package com.jd.service;

import com.jd.models.Hero;

import java.util.List;

public interface HeroService {

    public Hero findByName(String name);

    public Hero save(Hero hero);

    public void delete(Hero hero);

    public List<Hero> findAll();

    public Hero findOne(long id);
}
