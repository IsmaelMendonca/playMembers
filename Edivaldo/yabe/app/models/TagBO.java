package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name="tb_tag",schema="public")
public class TagBO extends Model implements Comparable<TagBO> {
	
	@Required
	@Column(name="name", nullable=false)
	private String name;
	
	private TagBO(String name){
		this.name = name;
	}

	@Override
	public int compareTo(TagBO otherTag) {
		return name.compareTo(otherTag.name);
	}
	
	public static TagBO findOrCreateByName(String name){
		TagBO tag = TagBO.find("byName", name).first();
		if(tag==null){
			tag = new TagBO(name);
		}
		return tag;
	}
	public List<TagBO> findByTags() {
		return find("order by name ").from(1).fetch();
		
	}
	
	@Override
	public String toString(){
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
