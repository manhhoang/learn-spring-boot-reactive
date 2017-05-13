package com.jd.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "skill")
public class Skill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long skillId;

	// The super skill's name
	@NotNull
	private String name;

	public Skill() {
	}

	public Skill(String name) {
		this.name = name;
	}

	// Getter and setter methods

	public String getName() {
		return name;
	}

	public long getSkillId() {
		return skillId;
	}

	public void setSkillId(long skillId) {
		this.skillId = skillId;
	}

	public void setName(String name) {
		this.name = name;
	}
}
