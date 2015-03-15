package response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IsUserAuthenticated implements Serializable{

	private static final long serialVersionUID = 1937913962447010966L;
	private String response;
	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	
	
}
