package com.synchronous.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synchronous.exception.ApiException;
import com.synchronous.models.Hero;
import com.synchronous.repository.HeroRepository;
import com.synchronous.service.HeroServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class HeroController {

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private HeroServiceImpl heroService;

    @RequestMapping(value = "/hero/create", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    ResponseEntity<Hero> create(@RequestBody Hero hero) throws ApiException {
        Hero foundHero = heroService.findByName(hero.getName());
        if (foundHero != null)
            throw new ApiException("100", "Hero is exist");

        Hero newHero;
        try {
            newHero = heroService.save(hero);
        } catch (Exception ex) {
            throw new ApiException("100", "Fail to save hero");
        }
        return new ResponseEntity<>(newHero, HttpStatus.OK);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long id) {
        try {
            Hero hero = new Hero(id);
            heroService.delete(hero);
        } catch (Exception ex) {
            return "Error deleting the super hero:" + ex.toString();
        }
        return "Super hero succesfully deleted!";
    }

    @RequestMapping(value = "/hero/{name}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String getByName(@PathVariable("name") String name) throws ApiException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Hero superHero = heroService.findByName(name);
            if (superHero == null)
                throw new ApiException("100", "Fail to find hero");
            return mapper.writeValueAsString(superHero);
        } catch (Exception ex) {
            throw new ApiException("100", "Fail to find hero");
        }
    }

    @RequestMapping(value = "/heroes", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<List<Hero>> getAllHeroes() throws ApiException {
        List<Hero> heroes;
        try {
            heroes = heroService.findAll();
        } catch (Exception ex) {
            throw new ApiException("100", "Fail to find all hero");
        }
        return new ResponseEntity<>(heroes, HttpStatus.OK);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String updateHero(long id, String name) throws ApiException {
        try {
            Hero hero = heroService.findOne(id);
            hero.setName(name);
            heroRepository.save(hero);
        } catch (Exception ex) {
            throw new ApiException("100", "Fail to update hero");
        }
        return "Super hero succesfully updated!";
    }

}