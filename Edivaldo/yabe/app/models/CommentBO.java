package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import controllers.CRUD.Hidden;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name="tb_comment",schema="public")
public class CommentBO extends Model {
	
	@Required
	@Column(name="author",nullable=false)
	private String author;
	
	@Hidden
	@Column(name="postedAt", nullable=false )
	private Date postedAt;

	@ManyToOne
	@Required
	@JoinColumn(name="id_post", nullable=false)
	private PostBO post;
	
	@Lob
	@Required
	@MaxSize(10000)
	@Column(name="content",length=10000, nullable=false)
	private String content;
	
	public CommentBO(){
		super();
		this.postedAt = new Date();
	}
	
	public CommentBO(PostBO post, String author, String content){
		this.post = post;
		this.author = author;
		this.content = content;
		this.postedAt = new Date();
	}
	@Override
	public String toString() {
		return this.author;
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
