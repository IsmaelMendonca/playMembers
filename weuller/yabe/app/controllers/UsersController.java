package controllers;
 
import models.UserBO;
import controllers.CRUD.For;
import play.*;
import play.mvc.*;
 
@Check("admin")
@For(UserBO.class)
@With(Secure.class)
public class UsersController extends CRUD {    
}