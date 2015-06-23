package controllers;
 
import models.UserBO;
import controllers.CRUD.For;
import play.mvc.With;
 
@Check("admin")
@For(UserBO.class)
@With(Secure.class)
public class UsersController extends CRUD {    
}