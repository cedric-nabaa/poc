package test;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import model.User;
import requests.AuthenticateUserRequest;
import requests.SignupRequest;
import response.ExceptionResponse;
import response.GenericResponse;
import connection.MongoConnectionSingleton;
import dao.UserDAO;
import exceptions.IdAlreadyExistsException;
import exceptions.UnauthenticatedUserException;


@Path("/users")
public class TestRest extends ResourceConfig{
	
	private static final Logger logger= Logger.getLogger(TestRest.class.getName());
	
	private UserDAO userDao=null;
	

	
	public TestRest(){
		register(CORSResponseFilter.class);
		property(ServerProperties.TRACING, "ALL");
		MongoConnectionSingleton mc = MongoConnectionSingleton.getInstance();
		userDao=new UserDAO(mc.getMongoDB());
	}
	
//	@GET
//	@Path("/get/{username}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public User getUserById(@PathParam("username") String username) {
//		return userDao.readUser(username);
//	}
	
	
	@GET
	@Path("/get/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	//TODO change email
	public Response getUserById(@PathParam("username") String email) {
		
		return Response.ok().entity(userDao.findUserByMail(email))
				//.header("Access-Control-Allow-Origin", "*")
				.build();  
				
				
	}
	
	
/*
 * 
 * 
 * var xhr= new XMLHttpRequest();
 xhr.open("POST", "http://localhost:8081/Studant/rest/users/login");
 xhr.onreadystatechange = function() {
     if (xhr.readyState == 4) {
         alert(xhr.responseText);
      }
  }
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.send(JSON.stringify({"email":"ced@hot.com","password":"123"}}));
 */
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateUser(AuthenticateUserRequest authenticateUserRequest){
		Response res=null;
		ExceptionResponse er = null;
		//GenericResponse genericResponse = new GenericResponse();
		
		
		try{
			String email = authenticateUserRequest.getEmail();
			String password = authenticateUserRequest.getPassword();
			if(email==null){
				throw new IllegalArgumentException("missing email");
			}
			if(password==null){
				throw new IllegalArgumentException("missing  password");
			}
			
			userDao.validateUser(email, password);
			User user =userDao.findUserByMail(email);
			user.setPassword(null);
			res =Response.ok().entity(user).build();
			
		}catch(IllegalArgumentException iae){
			
			er=new ExceptionResponse();
			er.setType("IllegalArgumentException");
			er.setMessage("missing required argument");
			logger.log(Level.INFO,"isUserAuthenticated",iae);
			
			res= Response.ok().entity(er).build();
		}catch(UnauthenticatedUserException uue){
			logger.log(Level.INFO,"isUserAuthenticated",uue);
			er= new ExceptionResponse();
			er.setType("UnauthenticatedUserException");
			er.setMessage("failed to authenticate user");
			res= Response.ok().entity(er).build();
		}
		System.out.println(res.getEntity());
		return res;
	}
	
	/*
	 * 
	 * 
	 * 
	 * var xhr= new XMLHttpRequest();
 xhr.open("POST", "http://localhost:8081/Studant/rest/users/signup");
 xhr.onreadystatechange = function() {
     if (xhr.readyState == 4) {
         alert(xhr.responseText);
      }
  }
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.send(JSON.stringify({"user":{"password":"123","email":"ced@hot.com", "firstName":"cedric","lastName":"nabaa"}})); * 
	 * 
	 * 
	 */
	
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(SignupRequest request){
		Response res =null;
		
		try{

			if(request ==null || request.getUser()==null){
				throw new IllegalArgumentException("Wrong request");
			}
			
			User  user = request.getUser();
			
			
			
			if(user.getPassword()==null){
				throw new IllegalArgumentException("password is missing");
			}
			if(user.getEmail()==null){
				throw new IllegalArgumentException("email is missing");
			}
			if(user.getFirstName()==null){
				throw new IllegalArgumentException("fname is missing");
			}
			if(user.getLastName()==null){
				throw new IllegalArgumentException("lname is missing");
			}
			userDao.createUser(user);
			res =Response.ok("ok").build();
			
		}
		catch(IdAlreadyExistsException ie){
			res= Response.serverError().entity("email already exist").build();
		}
		catch (IllegalArgumentException iae){
			System.out.println(iae.getMessage());
			res= Response.serverError().entity(iae.getMessage()).build();
		}
		return res;
	}
	
}
