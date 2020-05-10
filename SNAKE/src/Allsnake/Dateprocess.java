/**
 * 
 */
package Allsnake;

/**
 * @author Walege
 *
 */
public class Dateprocess implements Runnable {
private Player[] playerlist;	
public Dateprocess(Player[] playlist) {
		// TODO Auto-generated constructor stub
		playerlist = playlist;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	/**
	 * 
	 */

	public void run() {
		// TODO Auto-generated method stub
		if(playerlist!=null) {
		for(int i = 0 ; i < playerlist.length; i++)
		{
			playerlist[i].getSnake().setNext_direction(playerlist[i].Getbuffer().take(playerlist[i].getSnake().getDirection()));
			playerlist[i].getSnake().setDirection(playerlist[i].getSnake().getNext_direction());
			playerlist[i].getSnake().moveSnake();
		}
		
		}else {
			try {
			this.wait();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
