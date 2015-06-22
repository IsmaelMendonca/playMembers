package controllers;
 
import models.TagBO;
import controllers.CRUD.For;
import play.*;
import play.mvc.*;
 
@Check("admin")
@For(TagBO.class)
@With(Secure.class)
public class TagsController extends CRUD {    
}