package response;

import java.io.Serializable;

public class GenericResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
//	public GenericResponse(String response){
//		this.response=response;
//	}
	private String response;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	

}
