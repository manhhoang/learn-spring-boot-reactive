package com.jd.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.models.Skill;
import com.jd.models.SuperHero;
import com.jd.models.SuperHeroDao;

/**
 * A class to test interactions with the MySQL database using the SuperHeroDao
 * class.
 *
 * @author Manh Hoang
 */
@Controller
public class SuperHeroController {

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * /create --> Create a new super hero and save it in the database.
	 * 
	 * @param name
	 *            Super Hero's name
	 * @param pseudonym
	 *            Super Hero's pseudonym
	 * @param publisher
	 *            Super Hero's publisher
	 * @param skills
	 *            Super Hero's skills
	 * @param allies
	 *            Super Hero's allies
	 * @param dateOfAppearance
	 *            Super Hero's date of first appearance
	 * @return A string describing if the super hero is succesfully created or
	 *         not.
	 */
	// @RequestMapping("/create")
	// @ResponseBody
	// public String create(String name, String pseudonym, String publisher,
	// List<String> skills, List<String> allies,
	// String dateOfAppearance) {
	// SuperHero superHero = null;
	// try {
	// superHero = new SuperHero(name, pseudonym, publisher, skills, allies,
	// dateOfAppearance);
	// superHeroDao.save(superHero);
	// } catch (Exception ex) {
	// return "Error creating the super hero: " + ex.toString();
	// }
	// return "User succesfully created! (id = " + superHero.getId() + ")";
	// }

	@RequestMapping("/create")
	@ResponseBody
	public String create(String name, String pseudonym, String publisher, String skill, String dateOfAppearance) {
		SuperHero superHero = null;
		try {
			String[] skills = skill.split(",");
			Set<Skill> sks = new HashSet<>();
			for (String s : skills) {
				Skill sk = new Skill(s);
				sks.add(sk);
			}
			superHero = new SuperHero(name, pseudonym, publisher, sks, dateOfAppearance);
			superHeroDao.save(superHero);
		} catch (Exception ex) {
			return "Error creating the super hero: " + ex.toString();
		}
		return "User succesfully created! (id = " + superHero.getId() + ")";
	}

	/**
	 * /delete --> Delete the super hero having the passed id.
	 * 
	 * @param id
	 *            The id of the superHero to delete
	 * @return A string describing if the user is succesfully deleted or not.
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(long id) {
		try {
			SuperHero superHero = new SuperHero(id);
			superHeroDao.delete(superHero);
		} catch (Exception ex) {
			return "Error deleting the super hero:" + ex.toString();
		}
		return "Super hero succesfully deleted!";
	}

	/**
	 * /get-by-name --> Return the id for the super hero having the passed name.
	 * 
	 * @param name
	 *            The name to search in the database.
	 * @return The super hero id or a message error if the super hero is not
	 *         found.
	 */
	@RequestMapping("/get-by-name")
	@ResponseBody
	public String getByName(String name) {
		String superHeroId;
		try {
			SuperHero superHero = superHeroDao.findByName(name);
			superHeroId = String.valueOf(superHero.getId());
		} catch (Exception ex) {
			return "Super hero not found";
		}
		return "The super hero id is: " + superHeroId;
	}

	/**
	 * /get-all --> Return all the super heroes.
	 * 
	 * @return All the super heroes id or a message error if the super hero is
	 *         not found.
	 */
	@RequestMapping("/get-all")
	@ResponseBody
	public String getAll() {
		ObjectMapper mapper = new ObjectMapper();
		List<SuperHero> superHeroes = null;
		try {
			superHeroes = (List<SuperHero>) superHeroDao.findAll();
			return mapper.writeValueAsString(superHeroes);
		} catch (Exception ex) {
			return "Super hero not found";
		}
	}

	/**
	 * /update --> Update the name for the super hero in the database having the
	 * passed id.
	 * 
	 * @param id
	 *            The id for the super hero to update.
	 * @param name
	 *            The new name.
	 * @return A string describing if the super hero is succesfully updated or
	 *         not.
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String updateSuperHero(long id, String name) {
		try {
			SuperHero superHero = superHeroDao.findOne(id);
			superHero.setName(name);
			superHeroDao.save(superHero);
		} catch (Exception ex) {
			return "Error updating the super hero: " + ex.toString();
		}
		return "Super hero succesfully updated!";
	}

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	@Autowired
	private SuperHeroDao superHeroDao;

} // class SuperHeroController