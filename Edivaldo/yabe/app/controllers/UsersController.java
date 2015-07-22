package controllers;

import controllers.CRUD.For;
import models.UserBO;
import play.mvc.With;


@Check("admin")
@For(UserBO.class)
@With(Secure.class)
public class UsersController extends CRUD {

}
