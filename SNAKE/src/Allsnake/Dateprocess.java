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
private int star=0,end =0;
public Dateprocess(Player[] playlist,int starnumber,int endnumber) {
		// TODO Auto-generated constructor stub
		playerlist = playlist;
		star = starnumber;
		end=endnumber;
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
		for(int i = star ; i < end; i++)
		{
			playerlist[i].getSnake().setNext_direction(playerlist[i].Getbuffer().take(playerlist[i].getSnake().getDirection()));
			playerlist[i].getSnake().setDirection(playerlist[i].getSnake().getNext_direction());
			playerlist[i].getSnake().moveSnake();
		}
		
		}
//		else {
//			try {
//			this.wait();
//			}catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
	}

}
