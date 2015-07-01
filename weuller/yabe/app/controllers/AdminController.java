package controllers;
import java.util.List;

import models.PostBO;
import models.TagBO;
import models.UserBO;
import play.i18n.Lang;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
 

 
@With(Secure.class)
public class AdminController extends Controller {
    
    @Before
    static void setConnectedUser() {
        if(SecurityController.isConnected()) {
            UserBO user = UserBO. getUserByEmail(SecurityController.connected());
            renderArgs.put("user", user.getFullname());
        }
    }
 
    public static void index() {
        String user = SecurityController.connected();
        List<PostBO> posts = PostBO.getPostListForUser(user);
        render(posts);
    }
    
    public static void selectlanguage(String language, String currentURL){
    	Lang.change(language);
    	redirect(currentURL);
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
            // Create post
            UserBO author = UserBO.getUserByEmail(SecurityController.connected());
            post = new PostBO(author, title, content);
        }
        else {
            // Retrieve post
            post = PostBO.findById(id);
            // Edit
            post.setTitle(title);
            post.setContent(content); 
            post.getTags().clear();
        }
        // Set tags list
        for(String tag : tags.split("\\s+")) {
            if(tag.trim().length() > 0) {
				post.getTags().add(TagBO.findOrCreateByName(tag));
            }
        }
        // Validate
        validation.valid(post);
        if(validation.hasErrors()) {
            render("@form", post);
        }
        // Save
        post.save();
        index();
    }
}