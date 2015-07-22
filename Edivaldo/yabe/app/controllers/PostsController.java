package controllers;

import controllers.CRUD.For;
import models.PostBO;
import play.mvc.With;

@Check("admin")
@For(PostBO.class)
@With(Secure.class)
public class PostsController extends CRUD{

}
