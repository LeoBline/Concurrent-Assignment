package Allsnake;

/**
 * Update snake data imlement multithreading
 */
public class DateProcess implements Runnable {
	//The player array
private Player[] playerlist;
    //The start number of snakes
private int star = 0;
	//The end number of snakes
private int end = 0;

	/**
	 * The game player count
	 * @param playlist
	 * @param startNumber
	 * @param endNumber
	 */
	public DateProcess(Player[] playlist, int startNumber, int endNumber) {
		// TODO Auto-generated constructor stub
		playerlist = playlist;
		star = startNumber;
		end = endNumber;
	}

	/**
	 * Multithreaded run method
	 */
	public void run() {
		// TODO Auto-generated method stub
		if(playerlist!=null) {
		for(int i = star ; i < end; i++)
			{
				if(playerlist[i].getIsRobot()==true) {
					//add Random move the robot buffer
					playerlist[i].getPlayerBuffer().append(playerlist[i].getSnake().RandomDirection());
				
				}
				playerlist[i].getSnake().setNext_direction(playerlist[i].getPlayerBuffer().take(playerlist[i].getSnake().getDirection()));
				playerlist[i].getSnake().setDirection(playerlist[i].getSnake().getNext_direction());
				playerlist[i].getSnake().moveSnake();
			}
		}

	}

}
