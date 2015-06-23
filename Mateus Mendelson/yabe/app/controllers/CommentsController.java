package controllers;
 
import models.CommentBO;
import play.mvc.With;
import controllers.CRUD.For;

@Check("admin")
@For(CommentBO.class)
@With(Secure.class)
public class CommentsController extends CRUD {    
}