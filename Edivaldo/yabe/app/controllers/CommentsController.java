package controllers;

import controllers.CRUD.For;
import models.CommentBO;
import play.mvc.With;

@Check("admin")
@For(CommentBO.class)
@With(Secure.class)
public class CommentsController extends CRUD {

}
