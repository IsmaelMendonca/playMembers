package models;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
 
@Table(name = "tb_post")
@Entity
public class PostBO extends Model {
 
    @Required
    @Column(name = "title")
    private String title;
    
    @Required
    @Column(name = "postedAt")
    private Date postedAt;
    
    @Lob
    @Required
    @MaxSize(10000)
    @Column(name = "content")
    private String content;
    
    @Required
    @ManyToOne
    @JoinColumn(name = "id_author")
    private UserBO author;
    
    @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
    private List<CommentBO> comments;
    

    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable(name = "rl_post_tag", joinColumns = { @JoinColumn(name = "id_post") }, inverseJoinColumns = { @JoinColumn(name = "id_tag") })
    private Set<TagBO> tags;
     
    public PostBO(UserBO author, String title, String content) {
        this.comments = new ArrayList<CommentBO>();
        this.tags = new TreeSet<TagBO>();
        this.author = author;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
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

	public UserBO getAuthor() {
		return author;
	}

	public void setAuthor(UserBO author) {
		this.author = author;
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

	public PostBO() {
		super();
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
    
    public PostBO tagItWith(String name) {
        tags.add(TagBO.findOrCreateByName(name));
        return this;
    }
    
    public static List<PostBO> findTaggedWith(String... tags) {
        return PostBO.find(
                "select distinct p from PostBO p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.title, p.content,p.postedAt having count(t.id) = :size"
        ).bind("tags", tags).bind("size", tags.length).fetch();
    }

	@Override
	public String toString() {
//		return "Post [title=" + title + ", postedAt=" + postedAt + ", content="
//				+ content + ", author=" + author + ", comments=" + comments
//				+ ", tags=" + tags + "]";
		
		return title;
	} 	
}