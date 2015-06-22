package controllers;
 
import models.CommentBO;
import controllers.CRUD.For;
import play.*;
import play.mvc.*;
 
@Check("admin")
@For(CommentBO.class)
@With(Secure.class)
public class CommentsController extends CRUD {    
}