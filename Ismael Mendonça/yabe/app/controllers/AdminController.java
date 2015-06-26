package controllers;

import java.util.List;

import models.PostBO;
import models.TagBO;
import models.UserBO;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class AdminController extends Controller {
	
	@Before
	static void setConnectedUser() {
		
		if(SecurityController.isConnected()) {
			UserBO user = UserBO.find("byEmail", SecurityController.connected()).first();
			renderArgs.put("user", user.getFullname());
		}
	}
	
	public static void index() {
		
		String user = SecurityController.connected();
		List<PostBO> posts = PostBO.find("author.email", user).fetch();
		String msgPost = Messages.get("post.post");
		String msgPosts = Messages.get("post.posts");
		render(posts, msgPost, msgPosts);
	}
	
	public static void form(Long id) {
		
		if(id != null) {
			PostBO post = PostBO.findById(id);
			render(post);
		}
		render();
	}
	
	public static void save(Long id, String title, String content, String tags) {
		
		PostBO post;
		if(id == null) {
			UserBO author = UserBO.find("byEmail", SecurityController.connected()).first();
			post = new PostBO(author, title, content);
		} else {
			post = PostBO.findById(id);
			
			post.setTitle(title);
			post.setContent(content);
			post.getTags().clear();
		}
		
		for(String tag : tags.split("\\s+")) {
			if(tag.trim().length() > 0) {
				post.getTags().add(TagBO.findOrCreateByName(tag));
			}
		}
		
		validation.valid(post);
		if(validation.hasErrors()) {
			render("@form", post);
		}
		
		post.save();
		index();
	}
	
	public static void changeLanguage(String language, String currentPage) {
		
		Lang.change(language);
		redirect(currentPage);
	}
	
	@Before
	public static void getCurrentLanguage() {
		
		renderArgs.put("lang", Lang.get());
	}
}