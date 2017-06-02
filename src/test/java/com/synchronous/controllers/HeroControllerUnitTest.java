package com.synchronous.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.synchronous.models.Hero;
import com.synchronous.repository.HeroRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HeroControllerUnitTest {

  @Mock
  private HeroRepository heroRepository;

  @Mock
  private Hero hero;

  @Before
  public void setupMock() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testMockCreation() {
    assertNotNull(hero);
    assertNotNull(heroRepository);
  }

  @Test
  public void testGetAllHero() {
    List<Hero> heroes = new ArrayList<>();
    Hero hero = new Hero("Batman");
    heroes.add(hero);
    when(heroRepository.findAll()).thenReturn(heroes);
    for (Hero foundHero : heroRepository.findAll()) {
      assertEquals(foundHero.getName(), "Batman");
    }
  }

  @Test
  public void testGetByName() {
    when(heroRepository.findByName("Batman")).thenReturn(hero);
    assertEquals(hero, heroRepository.findByName("Batman"));
  }

  @Test
  public void testCreateHero() {
    when(heroRepository.save(hero)).thenReturn(hero);
    assertNotNull(heroRepository.save(hero));
  }

}
