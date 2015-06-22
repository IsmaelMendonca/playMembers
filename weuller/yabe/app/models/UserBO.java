package models;
 
import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
 
@Table(name = "tb_user")
@Entity //Identifica a classe como uma entidade a ser persistida. 
public class UserBO extends Model {
 
    @Email
    @Required
    @Column(name = "email")
    @Unique
    private String email;

    @Password
    @Required
    @Column(name = "password")
    private String password;
    
    @Column(name = "fullname")
    private String fullname;
    
    @Column(name = "isAdmin")
    private boolean isAdmin;
    
	public UserBO(String email, String password, String fullname) {
//		super();
		this.email = email;
		this.password = password;
		this.fullname = fullname;
	}	
		
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email != null ? email.toLowerCase() : null;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public static UserBO connect(String email, String password) {
	    return find("byEmailAndPassword", email, password).first();
	}

	@Override
	public String toString() {
		return email;
	}
}
