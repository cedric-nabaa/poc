package dao;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.MongoException.DuplicateKey;

import connection.MongoConnectionSingleton;
import exceptions.IdAlreadyExistsException;
import exceptions.UnauthenticatedUserException;

public class UserDAO {
	
	private DBCollection col;
	private static final Logger logger = Logger.getLogger(UserDAO.class.getName());
	
	public UserDAO(DB mongoDB){
		if(mongoDB==null){
			throw new IllegalArgumentException("");
		}
		col=mongoDB.getCollection("users");
	}
	
	public User findUserByMail(String email){
		
		DBObject query= BasicDBObjectBuilder.start().append("email", email).get();
		DBObject data = col.findOne(query);
		if(data!=null){
			return User.toUser(data);
		}else{
			//TODO needs to log the wrong usernames
			return new User();
		}
		
	}
	
	public void validateUser(String email, String password) throws UnauthenticatedUserException{
		
	
		
		User user =findUserByMail(email);
//		if(!password.equals(user.getPassword())){
//			throw new UnauthenticatedUserException();
//		}
		System.out.println("userPass " + user.getPassword());
		if(user.getPassword()==null || password==null){
			throw new UnauthenticatedUserException();
		}
		validatePassword(user.getPassword(),password);

		
	}
	public void createUser(User user) throws IdAlreadyExistsException{
		try{
			user.setPassword(generatePasswordHash(user.getPassword()));
			DBObject dbObject=user.toDBObject();
			col.insert(dbObject);
		}catch(DuplicateKey dk){
			logger.log(Level.INFO,"DuplicateKey",dk);
			throw new IdAlreadyExistsException(dk);
		}catch(MongoException me){
			logger.log(Level.INFO,"MongoException",me);
			throw new RuntimeException(me);
		}
		
		
	}
	
	private static String generatePasswordHash(String password){
		try{
			
			Random sr = new SecureRandom();
			byte[] salt = new byte[16];
			sr.nextBytes(salt);
			
			String saltS=Base64.getEncoder().encodeToString(salt);
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.update(password.getBytes("UTF-8"));
			md.update(saltS.getBytes("UTF-8"));
			byte[] hash = md.digest();
			return saltS+Base64.getEncoder().encodeToString(hash);
			//return sun.misc.BASE64Encoder().encode(hash);
			
		}catch(NoSuchAlgorithmException | UnsupportedEncodingException ne){
			logger.log(Level.WARNING,"encryption", ne);
			throw new RuntimeException(ne);
		}
	}
	private static void validatePassword(String hashedPassword, String password) throws UnauthenticatedUserException{
		
		try {
			String salt= hashedPassword.substring(0,hashedPassword.length()-44);
			//byte [] dSalt = Base64.getDecoder().decode(salt);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			md.update(password.getBytes("UTF-8"));
			md.update(salt.getBytes("UTF-8"));
			byte[] hash = md.digest();
			String generatedPass=salt+Base64.getEncoder().encodeToString(hash);
			if(!generatedPass.equals(hashedPassword)){
				throw new UnauthenticatedUserException();
			}
		} catch (NoSuchAlgorithmException  | UnsupportedEncodingException ne) {
			logger.log(Level.WARNING,"encryption", ne);
			throw new RuntimeException(ne);
		}
		
	}
	public static void main(String [] args) throws Exception{
		validatePassword("DNUsM+xqRCimRN5MPEEY8g==3gp1xByhB3itNI/LtqP34G95QAgEF83/UpTeWZ6i6Vw=", "123");
//		String hashedPassword="[B@437b379cXTpDPKiSGW45RKnOLAvDQI+DDAayRUKGJHTz8JbZvqo=";
//		String salt= hashedPassword.substring(0,hashedPassword.length()-44);
//		System.out.println(salt.length());
//		System.out.println(salt);
//		//byte [] dSalt = Base64.getDecoder().decode(salt);
//		MessageDigest md = MessageDigest.getInstance("SHA-256");
//		
//	
//		md.update("123".getBytes("UTF-8"));
//		md.update(salt.getBytes("UTF-8"));
//		byte[] hash = md.digest();
//		String generatedPass=salt+Base64.getEncoder().encodeToString(hash);
//		System.out.println(generatedPass);
	}
}
