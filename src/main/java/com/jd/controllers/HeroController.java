package com.jd.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.exception.ApiException;
import com.jd.models.Hero;
import com.jd.models.Skill;
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
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static reactor.bus.selector.Selectors.$;

@Controller
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
      throw new ApiException();

    Hero newHero;
    try {
      newHero = heroService.save(hero);
    } catch (Exception ex) {
      throw new ApiException();
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
      throw new ApiException();
    }
    return new ResponseEntity<>(heroes, HttpStatus.OK);
  }

  /**
   * /update --> Update the name for the super hero in the database having the passed id.
   * 
   * @param id The id for the super hero to update.
   * @param name The new name.
   * @return A string describing if the super hero is succesfully updated or not.
   */
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