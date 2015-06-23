package controllers;
 
import models.UserBO;
import play.mvc.With;
import controllers.CRUD.For;

@Check("admin")
@For(UserBO.class)
@With(Secure.class)
public class UsersController extends CRUD {    
}