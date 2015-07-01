package controllers;
 
import models.TagBO;
import play.mvc.With;
import controllers.CRUD.For;

 
@Check("admin")
@For(TagBO.class)
@With(Secure.class)
public class TagsController extends CRUD {    
}