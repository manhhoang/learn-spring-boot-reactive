package com.jd.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

/**
 * A DAO for the entity SuperHero is simply created by extending the
 * CrudRepository interface provided by spring. The following methods are some
 * of the ones available from such interface: save, delete, deleteAll, findOne
 * and findAll. The magic is that such methods must not be implemented, and
 * moreover it is possible create new query methods working only by defining
 * their signature!
 * 
 * @author Manh Hoang
 */
@Transactional
public interface SuperHeroDao extends CrudRepository<SuperHero, Long> {

	/**
	 * Return the super hero having the passed name or null if no super hero is
	 * found.
	 * 
	 * @param name
	 *            the super hero name.
	 */
	public SuperHero findByName(String name);

} // class SuperHeroDao
