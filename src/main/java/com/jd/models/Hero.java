package com.jd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "hero")
public class Hero {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long heroId;

	@NotNull
	private String name;

	private String pseudonym;

	private String publisher;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "hero_skill", joinColumns = { @JoinColumn(name = "hero_id") }, inverseJoinColumns = {
			@JoinColumn(name = "skill_id") })
	private Set<Skill> skills;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "hero_allies", joinColumns = {
			@JoinColumn(name = "hero_id", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "ally_hero_id", nullable = false) })
	private Set<Hero> allies;

	private String dateOfAppearance;

	public Hero() {
	}

	public Hero(long id) {
		this.heroId = id;
	}

	public Hero(String name) {
		this.name = name;
	}

	public Hero(String name, String pseudonym, String publisher, Set<Skill> skills, Set<Hero> allies,
				String dateOfAppearance) {
		this.name = name;
		this.pseudonym = pseudonym;
		this.publisher = publisher;
		this.skills = skills;
		this.allies = allies;
		this.dateOfAppearance = dateOfAppearance;
	}

	// Getter and setter methods

	public String getName() {
		return name;
	}

	public long getHeroId() {
		return heroId;
	}

	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPseudonym() {
		return pseudonym;
	}

	public void setPseudonym(String pseudonym) {
		this.pseudonym = pseudonym;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}

	public Set<Hero> getAllies() {
		return allies;
	}

	public void setAllies(Set<Hero> allies) {
		this.allies = allies;
	}

	public String getDateOfAppearance() {
		return dateOfAppearance;
	}

	public void setDateOfAppearance(String dateOfAppearance) {
		this.dateOfAppearance = dateOfAppearance;
	}

}
