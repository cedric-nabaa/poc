package requests;

import java.io.Serializable;
import java.util.Date;

import model.User;

public class SignupRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
