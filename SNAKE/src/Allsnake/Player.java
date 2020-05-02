package Allsnake;

/**
 * The Player Class
 *
 */
public class Player implements Runnable{
    private String ID;
    private int myScore;
    private Snake mySnake;
    private Buffer playerBuffer;

    public void run() {

    }

    /**
     * Move the Snake on the map from what player press on the keyboard
     *
     * @param move The 'move' that get from player
     */
    public void getPlayerMove(char move){
        playerBuffer.append(move);
    }



    public void InitSnake(){

    }

    //Getter and setter
    public int getScore() {
        return myScore;
    }

    public void setScore(int score) {
        myScore = score;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


}
