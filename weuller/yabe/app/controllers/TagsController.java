package controllers;
 
import models.TagBO;
import controllers.CRUD.For;
import play.mvc.With;

 
@Check("admin")
@For(TagBO.class)
@With(Secure.class)
public class TagsController extends CRUD {    
}