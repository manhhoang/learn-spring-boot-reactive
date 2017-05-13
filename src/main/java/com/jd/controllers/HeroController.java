package com.jd.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.exception.ApiException;
import com.jd.models.Hero;
import com.jd.repository.HeroRepository;
import com.jd.service.HeroServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.bus.EventBus;

import java.util.List;

@Controller
@RequestMapping("/api")
public class HeroController {

  @Autowired
  private HeroRepository heroRepository;

  @Autowired
  private HeroServiceImpl heroService;

  @Autowired
  private EventBus eventBus;

  @RequestMapping(value = "/hero/create", method = RequestMethod.POST, headers = "Accept=application/json")
  public @ResponseBody Hero create(@RequestBody Hero hero) throws ApiException {
    Hero foundHero = heroRepository.findByName(hero.getName());
    if (foundHero != null)
      throw new ApiException("100", "Hero is exist");

    Hero newHero;
    try {
      newHero = heroService.save(hero);
    } catch (Exception ex) {
      throw new ApiException("100", "Fail to save hero");
    }
    return newHero;
  }

  @RequestMapping("/delete")
  @ResponseBody
  public String delete(long id) {
    try {
      Hero hero = new Hero(id);
      heroRepository.delete(hero);
    } catch (Exception ex) {
      return "Error deleting the super hero:" + ex.toString();
    }
    return "Super hero succesfully deleted!";
  }

  @RequestMapping("/get-by-name")
  @ResponseBody
  public String getByName(String name) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      Hero superHero = heroRepository.findByName(name);
      if (superHero == null)
        return "Super hero not found";
      return mapper.writeValueAsString(superHero);
    } catch (Exception ex) {
      return "Super hero not found";
    }
  }

  @RequestMapping(value = "/heroes", method = RequestMethod.GET, headers = "Accept=application/json")
  public ResponseEntity<List<Hero>> getAllHeroes() throws ApiException {
    List<Hero> heroes;
    try {
      heroes = (List<Hero>) heroRepository.findAll();
    } catch (Exception ex) {
      throw new ApiException("100", "Fail to find all hero");
    }
    return new ResponseEntity<>(heroes, HttpStatus.OK);
  }

  @RequestMapping("/update")
  @ResponseBody
  public String updateSuperHero(long id, String name) {
    try {
      Hero hero = heroRepository.findOne(id);
      hero.setName(name);
      heroRepository.save(hero);
    } catch (Exception ex) {
      return "Error updating the super hero: " + ex.toString();
    }
    return "Super hero succesfully updated!";
  }

}