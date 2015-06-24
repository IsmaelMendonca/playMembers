package controllers;

import models.UserBO;

public class SecurityController extends Secure.Security {
	static boolean authenticate(String username, String password) {
		return UserBO.connect(username, password) != null;
	}
	
	static void onDisconnected() {
	    ApplicationController.index();
	}
	
	static void onAuthenticated() {
	    AdminController.index();
	}
	
	static boolean check(String profile) {
	    if("admin".equals(profile)) {
	        return UserBO.find("byEmail", connected()).<UserBO>first().isAdmin();
	    }
	    return false;
	}
}