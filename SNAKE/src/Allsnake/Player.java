package Allsnake;

import sun.security.action.GetBooleanAction;

/**
 * The Player Class
 *
 */
public class Player {
    private String ID;
    private int myScore;
    private Snake mySnake;
    private Buffer playerBuffer = new Buffer();
    private static int game_size;
    private boolean IsRobot = false;



    /**
     * Move the Snake on the map from what player press on the keyboard
     *
     * @param move The 'move' that get from player
     */
    public void getPlayerMove(char move){
        playerBuffer.append(move);
    }

    public  Player(String id,int gamesize) {
    	this.ID = id;
		game_size=gamesize;
		int[][] a =new int[game_size * game_size][2];
		mySnake = new Snake(a);
	}
    public Buffer Getbuffer() {
    	return playerBuffer;
    }


    public void InitSnake(){
    	mySnake = new Snake(new int[game_size * game_size][2]);
    	playerBuffer = new Buffer();

    }

    //Getter and setter
    public int getScore() {
    	myScore = mySnake.getScore();
        return myScore;
    }
    public synchronized Snake getSnake() {
    	return mySnake;
    }

//    public void setScore(int score) {
//        myScore = score;
//    }

    public String getID() {
        return ID;
    }
    public void setIsRobot(boolean flag) {
    	this.IsRobot = flag;
    }
    public boolean getIsRobot() {
    	return this.IsRobot;
    }

//    public void setID(String ID) {
//        this.ID = ID;
//    }


}
