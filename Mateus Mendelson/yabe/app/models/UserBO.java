package models;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name = "tb_user")
@Entity
public class UserBO extends Model {
    
    @Email
    @Required
    @Column(name = "email")
    @Unique
    public String email;
    
    @Required
    @Column(name = "password")
    public String password;
    
    @Column(name = "fullname")
    public String fullname;
    
    @Column(name = "isAdmin")
    public boolean isAdmin;
    
    public UserBO(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }
    
    public static UserBO connect(String email, String password) {
    	return find("email = ?1 AND password = ?2", email, password).first();
    }
    
    public String toString() {
        return email;
    }
 
}