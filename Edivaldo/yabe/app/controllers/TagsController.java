package controllers;

import controllers.CRUD.For;
import models.TagBO;
import play.mvc.With;


@Check("admin")
@For(TagBO.class)
@With(Secure.class)
public class TagsController extends CRUD {

}
