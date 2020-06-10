package Allsnake;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 *ServerDB class be created as a callable thread will return a value to
 * show whether this account is existed in DB
 */
public class ServerDB implements Callable<String> {
	//Get player account for information in FileDB "PlayerInformation"
	DB db = DBMaker.newFileDB(new File("PlayerInformation"))
			.closeOnJvmShutdown()
			.encryptionEnable("password")
			.make();
	//Put the account information to the playerMap
	ConcurrentNavigableMap<String, String> playerMap = db.getTreeMap("playerinformation");
	private String id;
	private String password;

	public ServerDB(String id,String password) {
		this.id=id;
		this.password = password;
	}


	public String call() throws InterruptedException {
		// TODO Auto-generated method stub
		return Login(id, password);
	}

	/**
	 * Synchronized method to allow concurrent login
	 * @param id
	 * @param password
	 * @return If login successfully return id, else return "".
	 */
    public synchronized String Login(String id,String password) {
    	if(playerMap.containsKey(id)) {
    		if(playerMap.get(id).equals(password)) {
    			db.close();
    			return id;
    		}else {
    			db.close();
    			return ""; }
    	}else {
    		db.close();
    		return ""; }
    }

	/**
	 * Add some accounts into the map for login
	 * @param map
	 * @param db
	 */
	public void update(ConcurrentNavigableMap<String, String> map, DB db) {
    	if (!map.containsKey("001")) {
			map.put("001","123456");
			map.put("002","123456");
		}
    	//Update the db with new player accounts
    	db.commit();
    }

//Getters
	public synchronized ConcurrentNavigableMap<String, String> getMap(){
		return playerMap;
	}

	public synchronized DB getDB(){
		return db;
	}



}
