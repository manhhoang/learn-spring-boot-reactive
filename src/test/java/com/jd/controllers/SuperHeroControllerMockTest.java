package com.jd.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jd.models.SuperHero;
import com.jd.models.SuperHeroDao;

public class SuperHeroControllerMockTest {

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
	public void testGetByName() {
		when(superHeroDao.findByName("Batman")).thenReturn(superHero);
		assertEquals(superHero, superHeroDao.findByName("Batman"));
	}

}
