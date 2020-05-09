package Allsnake;

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
		mySnake = new Snake(new int[game_size * game_size][2]);
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

//    public void setScore(int score) {
//        myScore = score;
//    }

    public String getID() {
        return ID;
    }

//    public void setID(String ID) {
//        this.ID = ID;
//    }


}
