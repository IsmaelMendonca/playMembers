package models;
 
import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Table(name = "tb_tag")
@Entity
public class TagBO extends Model implements Comparable<TagBO> {
	@Required
	@Column(name = "name")
    public String name;
 
    private TagBO(String name) {
        this.name = name;
    }
 
    public String toString() {
        return name;
    }
 
    public int compareTo(TagBO otherTag) {
        return name.compareTo(otherTag.name);
    }
    
    public static TagBO findOrCreateByName(String name) {
        TagBO tag = TagBO.find("byName", name).first();
        if(tag == null) {
            tag = new TagBO(name);
        }
        return tag;
    }
    
    public static List<Map> getCloud() {
        List<Map> result = TagBO.find("select new map(t.name as tag, count(p.id) as pound) from Post p join p.tags as t group by t.name order by t.name").fetch();
        return result;
    }
}