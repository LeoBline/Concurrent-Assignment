
/**
 * 
 */
package Allsnake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executorService= Executors.newFixedThreadPool(4);
		ServerUIControl server= new ServerUIControl();
	}

}
