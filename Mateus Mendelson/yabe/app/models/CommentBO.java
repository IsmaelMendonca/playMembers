package models;
 
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Table(name = "tb_comment")
@Entity
public class CommentBO extends Model {
 
	@Required
	@Column(name = "author")
    private String author;
    
    @Required
    @Column(name = "postedAt")
    private Date postedAt;
     
    @Lob
    @Required
    @MaxSize(10000)
    @Column(name = "content")
    private String content;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @Required
    @JoinColumn(name = "id_post")
    private PostBO post;
    
    public CommentBO(PostBO post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PostBO getPost() {
		return post;
	}

	public void setPost(PostBO post) {
		this.post = post;
	}
 
}