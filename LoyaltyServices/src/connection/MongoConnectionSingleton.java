package connection;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;




import com.mongodb.DB;
import com.mongodb.MongoClient;


public class MongoConnectionSingleton {
	private static MongoConnectionSingleton instance;
	private static final Logger logger= Logger.getLogger(MongoConnectionSingleton.class.getName());
	private  DB mongoDB;
	
	
	private MongoConnectionSingleton() throws UnknownHostException{
		MongoClient mc = new MongoClient("localhost",27017);
		mongoDB= mc.getDB("loyalty");
	}
	
	
	public static MongoConnectionSingleton getInstance(){
		if(instance==null){
			try{
				instance= new MongoConnectionSingleton();
			}catch(UnknownHostException e){
				logger.log(Level.WARNING,"UnknownHost", e);
				throw new RuntimeException(e);
			}
			
		}
		return instance;
	}
	
	
	
	public DB getMongoDB() {
		return mongoDB;
	}
	
	
	
	
	
	
	

	

	
	
}
