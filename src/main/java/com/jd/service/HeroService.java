package com.jd.service;

import com.jd.models.Hero;

public interface HeroService {

    public Hero findByName(String name);

    public Hero save(Hero hero);
}
