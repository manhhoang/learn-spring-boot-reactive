package com.synchronous.service;

import com.synchronous.model.Hero;
import com.synchronous.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HeroServiceImpl implements HeroService {

    @Autowired
    private HeroRepository heroRepository;

    @Override
    public Hero findByName(String name) {
        return heroRepository.findByName(name);
    }

    @Override
    public Hero save(Hero hero) {
        return heroRepository.save(hero);
    }

    @Override
    public void delete(Hero hero) {
        heroRepository.delete(hero);
    }

    @Override
    public List<Hero> findAll() {
        return (List<Hero>) heroRepository.findAll();
    }

    @Override
    public Hero findOne(long id) {
        return heroRepository.findOne(id);
    }

}
