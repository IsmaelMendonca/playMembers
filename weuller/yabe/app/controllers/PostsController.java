package controllers;
 
import models.PostBO;
import play.mvc.With;
import controllers.CRUD.For;
 
@Check("admin")
@For(PostBO.class)
@With(Secure.class)
public class PostsController extends CRUD {    
}