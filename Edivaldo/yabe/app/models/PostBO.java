package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.binding.NoBinding;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;


/**
 * @author emelo
 *
 */
@Entity
@Table(name="tb_post", schema="public")
public class PostBO extends Model {
	
	@Required
	@ManyToOne
	@JoinColumn(name="id_author",nullable=false)
	private UserBO author;

	@Required
	@Column(name="title", nullable=false)
	private String title;

	@Lob
	@Required
	@MaxSize(10000)
	@Column(name="content", length=10000, nullable=false )
	private String content;

	@OneToMany( mappedBy = "post", cascade = CascadeType.ALL)
	private List<CommentBO> comments;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "rl_post_tag", joinColumns = { @JoinColumn(name = "id_post") },
			inverseJoinColumns = { @JoinColumn(name = "id_tag") }) 
	private Set<TagBO> tags;
	
	@NoBinding
	@Column(name="postedAt", nullable=false)
	private Date postedAt;
	
	public PostBO() {
		super();
		this.postedAt = new Date();
	}

	public PostBO(UserBO author, String title, String content) {
		this.comments = new ArrayList<CommentBO>();
		this.tags = new TreeSet<TagBO>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
	}

	public PostBO addComment(String author, String content) {
		CommentBO newComment = new CommentBO(this, author, content).save();
		this.comments.add(newComment);
		this.save();
		return this;
	}

	public PostBO previous() {
		return PostBO.find("postedAt < ? order by postedAt desc", postedAt).first();
	}

	public PostBO next() {
		return PostBO.find("postedAt > ? order by postedAt asc", postedAt).first();
	}

	public PostBO tagItWith(String nome) {
		tags.add(TagBO.findOrCreateByName(nome));
		return this;
	}

	public static List<PostBO> findTaggedWith(String... tags) {
		return GenericModel
				.find("select distinct p from Post p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.title, p.content,p.postedAt having count(t.id) = :size")
				.bind("tags", tags).bind("size", tags.length).fetch();
	}

	public static List<Map> getCloud() {
		List<Map> result = GenericModel
				.find("select new map(t.name as tag, count(p.id) as pound) from Post p join p.tags as t group by t.name order by t.name")
				.fetch();
		return result;
	}
	public static List<PostBO> findListPost(String user) {
			List<PostBO> posts = find("author.email",user).fetch(); 
		return posts;
	}
	public static PostBO findByFrontPost(){
		return find("order by postedAt desc").first();
	}
	public static List<PostBO> findByOlderPosts(){
		return  PostBO.find("order by postedAt desc").from(1).fetch(10);
	}

	@Override
	public String toString() {
		return this.title;
	}

	public UserBO getAuthor() {
		return author;
	}

	public void setAuthor(UserBO author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<CommentBO> getComments() {
		return comments;
	}

	public void setComments(List<CommentBO> comments) {
		this.comments = comments;
	}

	public Set<TagBO> getTags() {
		return tags;
	}

	public void setTags(Set<TagBO> tags) {
		this.tags = tags;
	}
	

}
