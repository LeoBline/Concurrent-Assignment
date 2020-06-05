package Allsnake;
import org.mapdb.*; 
import java.io.File; 
import java.io.Serializable; 
import java.util.HashMap; 
import java.util.concurrent.ConcurrentNavigableMap; 

public class ServerDB {
        // configure and open database using builder pattern. 
        // all options are available with code auto-completion. 

 
    public synchronized String Login(String id,String password) {
    	//every time login will open the file db when login succeess or file will close the db.
    	      DB db = DBMaker.newFileDB(new File("PlayerInformation")) 
                .closeOnJvmShutdown() 
                .encryptionEnable("password") 
                .make();         
        ConcurrentNavigableMap<String, String> Playermap = db.getTreeMap("playerinformation");  
    	
    	
    	if(Playermap.containsKey(id)) {
    		if(Playermap.get(id).equals(password)) {
    			db.close();
    			return id;
    		}else
    		{db.close();
    			return "";
    		}
    	}else
    	{
    		db.close();
    		return "";
    	}
    	
    }
//    public synchronized ConcurrentNavigableMap<String, String> getMap(){
//    	return Playermap;
//    }
//    public synchronized DB getDB(){
//    	return db;
//    }
//    public void Updata(ConcurrentNavigableMap<String, String> Playermap,DB db) {
//    	if (!Playermap.containsKey("001")) {
//						    	Playermap.put("001","123456");
//    	Playermap.put("002","123456");
//		}
//    	db.commit();
//    }
//    public synchronized void end(DB db) {
//    	db.close();
//    }
}
