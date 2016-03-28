package com.jd.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jd.models.SuperHero;
import com.jd.models.SuperHeroDao;

public class SuperHeroControllerUnitTest {

  @Mock
  private SuperHeroDao superHeroDao;

  @Mock
  private SuperHero superHero;

  @Before
  public void setupMock() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testMockCreation() {
    assertNotNull(superHero);
    assertNotNull(superHeroDao);
  }

  @Test
  public void testGetAllHero() {
    List<SuperHero> heroes = new ArrayList<>();
    SuperHero hero = new SuperHero("Batman");
    heroes.add(hero);
    when(superHeroDao.findAll()).thenReturn(heroes);
    for (SuperHero foundHero : superHeroDao.findAll()) {
      assertEquals(foundHero.getName(), "Batman");
    }
  }

  @Test
  public void testGetByName() {
    when(superHeroDao.findByName("Batman")).thenReturn(superHero);
    assertEquals(superHero, superHeroDao.findByName("Batman"));
  }

  @Test
  public void testCreateHero() {
    when(superHeroDao.save(superHero)).thenReturn(superHero);
    assertNotNull(superHeroDao.save(superHero));
  }

}
