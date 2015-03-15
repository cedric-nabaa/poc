package response;

import java.io.Serializable;

public class SignupResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String response;
	
	public SignupResponse(){
		
	}
	public SignupResponse(String response){
		this.response=response;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	

}
