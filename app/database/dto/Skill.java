package database.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "skill")
public class Skill {

	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String SKILL_CATEGORY = "skillCategory";
	public static final String SKILL_CATEGORY_ID =  SKILL_CATEGORY + "." + SkillCategory.ID;
	
	private Integer id;
	private SkillCategory skillCategory;
	private String name;
	
	@Id
	@Column(name = "skill_id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name = "skill_category_id")
	public SkillCategory getSkillCategory() {
		return skillCategory;
	}
	public void setSkillCategory(SkillCategory skillCategory) {
		this.skillCategory = skillCategory;
	}
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
