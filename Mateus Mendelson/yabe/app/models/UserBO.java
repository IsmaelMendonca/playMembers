package models;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Table(name = "tb_user")
@Entity
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
        this.email = email.toLowerCase();;
        this.password = password;
        this.fullname = fullname;
    }
    
    public static UserBO connect(String email, String password) {
    	return find("email = ?1 AND password = ?2", email, password).first();
    }
    
    public String toString() {
        return email;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
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

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public static UserBO findFirstByEmail (String email) {
		return UserBO.find("byEmail", email).<UserBO>first();
	}
}