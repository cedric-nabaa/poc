package model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;


public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	

	private String email;
	private String password;
	private String fname;
	private String lname;
	private String phoneNumber;
	
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	


	public String getFirstName() {
		return fname;
	}

	public void setFirstName(String firstName) {
		this.fname = firstName;
	}

	public String getLastName() {
		return lname;
	}

	public void setLastName(String lastName) {
		this.lname = lastName;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public  DBObject toDBObject() {
		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
		
		
		builder.append("_id", UUID.randomUUID().toString());
		
		if(this.getEmail()!=null){
			builder.append("email", this.getEmail());
		}
		
		if(this.getPassword()!=null){
			builder.append("pass",this.getPassword());
		}
		
		if(this.getFirstName()!=null){
			builder.append("fname", this.getFirstName());
		}
		if(this.getLastName()!=null){
			builder.append("lname", this.getLastName());
		}
		
		if(this.getPhoneNumber()!=null){
			builder.append("fone", this.getPhoneNumber());
		}
		return builder.get();
    }
 
    // convert DBObject Object to User
    public static User toUser(DBObject doc) {
        User user = new User();
        user.setPassword((String)doc.get("pass"));
        user.setEmail((String) doc.get("email"));
        if(doc.get("fname")!=null){
        	user.setFirstName((String)doc.get("fname"));
        }
        if(doc.get("lname")!=null){
            user.setLastName((String)doc.get("lname"));
        }
        return user;
    }
    
}
