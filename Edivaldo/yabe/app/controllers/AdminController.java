package controllers;

import java.util.List;

import controllers.Secure.Security;
import models.PostBO;
import models.TagBO;
import models.UserBO;
import play.data.validation.Validation;
import play.i18n.Lang;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class AdminController extends Controller {
	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			UserBO user = UserBO.findByMail(Security.connected());
			renderArgs.put("user", user.getFullname());
		}
	}

	public static void index() {
		String user = Security.connected();
		List<PostBO> posts = PostBO.findListPost(user);
		render(posts);
	}

	public static void form(Long id) {
		if (id != null) {
			PostBO post = PostBO.findById(id);
			render(post);
		}
		render();
	}

	public static void save(Long id, String title, String content, String tags) {
		PostBO post;
		if (id == null) {
			// Create post
			UserBO author = UserBO.findByMail(Security.connected());
			post = new PostBO(author, title, content);
		} else {
			// Retrieve post
			post = PostBO.findById(id);
			// Edit
			post.setTitle(title);
			post.setContent(content);
			post.getTags().clear();
		}

		// Set tags list
		for (String tag : tags.split("\\s+")) {
			if (tag.trim().length() > 0) {
				post.getTags().add(TagBO.findOrCreateByName(tag));
			}
		}
		// Validate
		validation.valid(post);
		if (Validation.hasErrors()) {
			render("@form", post);
		}
		// Save
		post.save();
		index();
	}
	public static void lang(String string,final String url){
    	Lang.change(string);
    	redirect(url);
    }
}
