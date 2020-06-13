package Allsnake;


public class DateProcess implements Runnable {
private Player[] playerlist;	
private int star = 0;
private int end = 0;

public DateProcess(Player[] playlist, int startNumber, int endNumber) {
		// TODO Auto-generated constructor stub
		playerlist = playlist;
		star = startNumber;
		end = endNumber;
	}

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
