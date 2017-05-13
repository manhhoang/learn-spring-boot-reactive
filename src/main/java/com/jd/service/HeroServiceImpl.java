package com.jd.service;

import com.jd.models.Hero;
import com.jd.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HeroServiceImpl implements HeroService, Consumer<Event<String>> {

    @Autowired
    private HeroRepository heroRepository;

    @Override
    public void accept(Event<String> stringEvent) {
        System.out.print(stringEvent.getData());
    }

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
