package controllers;

import java.io.IOException;
import java.util.List;

import models.PostBO;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;

public class ApplicationController extends Controller {
	private static String lng="pt-br";
	
	@Before
	static void addDefaults() {
		if(lng.equals("pt")) {
			renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title.pt"));
		    renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline.pt"));
		}
		else if(lng.equals("en")) {
		    renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title.en"));
		    renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline.en"));
		}
	}
	
	public static void show(Long id) {
	    PostBO post = PostBO.findById(id);
	    String randomID = Codec.UUID();
	    render(post, randomID);
	}

    public static void index() {
    	List<PostBO> posts = PostBO.findByPostedAtOrderDesc();
    	
    	PostBO frontPost = posts.get(0);
    	
    	List<PostBO> olderPosts = posts.subList(1, posts.size() < 10 ? posts.size() : 10);
    	
        render(frontPost, olderPosts);
    }
    
    public static void postComment(
            Long postId, 
            @Required(message="Author is required") String author, 
            @Required(message="A message is required") String content, 
            @Required(message="Please type the code") String code, 
            String randomID) 
    {
        PostBO post = PostBO.findById(postId);
        
        if(lng.equals("pt")) {
        	if(!Play.id.equals("test")) {
	            validation.equals(code, Cache.get(randomID)).message("Código inválido. Por favor, tente de novo");
	        }
	        
	        if(validation.hasErrors()) {
	            render("ApplicationController/show.html", post, randomID);
	        }
	        post.addComment(author, content);
	        flash.success("Obrigado por publicar, %s", author);
	        Cache.delete(randomID);
	        show(postId);
        }
        else if(lng.equals("en")) {
	        if(!Play.id.equals("test")) {
	            validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
	        }
	        
	        if(validation.hasErrors()) {
	            render("ApplicationController/show.html", post, randomID);
	        }
	        post.addComment(author, content);
	        flash.success("Thanks for posting %s", author);
	        Cache.delete(randomID);
	        show(postId);
        }
    }
    
    public static void captcha(String id) throws IOException {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#E4EAFD");
        Cache.set(id, code, "10mn");
        captcha.close();
        renderBinary(captcha);
    }
    
    public static void listTagged(String tag) {
        List<PostBO> posts = PostBO.findTaggedWith(tag);
        render(tag, posts);
    }
    
    public static void setLanguage(String language, String path) {
    	play.i18n.Lang.change(language);
    	lng = language;
    	redirect(path);
    }
}