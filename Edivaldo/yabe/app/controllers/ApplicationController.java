package controllers;

import java.io.IOException;
import java.util.List;

import models.PostBO;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;

public class ApplicationController extends Controller {

    public static void index() {
    	PostBO frontPost =  PostBO.findByFrontPost();
    	List<PostBO> olderPosts = PostBO.findByOlderPosts();
    	render(frontPost, olderPosts);
    } 
    @Before
    static void addDefaults(){
    	renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
    	renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }
    public static void show(Long id){
    	PostBO post = PostBO.findById(id);
    	render(post);
    }
    public static void postComment(Long postId,
    		@Required(message="Author is required!") String author,
    		@Required(message="A message is required!") String content,
    		@Required(message="Please type the code.") String code,
    		String randomId){
    	PostBO post = PostBO.findById(postId);
    	validation.equals(code, Cache.get(randomId)).message("Invalid code. Please type it again!");
    	if(Validation.hasErrors()) {
    		render("ApplicationController/show.html", post, randomId);
    	}
    	post.addComment(author, content);
    	flash.success("Thanks for posting %s !",author);
    	show(postId);
    }
    public static void captcha(String id) throws IOException{
    	Images.Captcha captcha = Images.captcha();
    	String code = captcha.getText("#E4EAFD");
    	Cache.set(id, code,"10mn");
    	captcha.close();
    	renderBinary(captcha);
    }
    public static void listTagged(String tag){
    	List<PostBO> posts = PostBO.findTaggedWith(tag);
    	render(tag, posts);
    }
}