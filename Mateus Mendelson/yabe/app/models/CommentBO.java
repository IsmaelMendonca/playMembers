package models;
 
import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Table(name = "tb_comment")
@Entity
public class CommentBO extends Model {
 
	@Required
	@Column(name = "author")
    public String author;
    
    @Required
    @Column(name = "postedAt")
    public Date postedAt;
     
    @Lob
    @Required
    @MaxSize(10000)
    @Column(name = "content")
    public String content;
    
    @ManyToOne
    @Required
    public PostBO post;
    
    public CommentBO(PostBO post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }
 
}