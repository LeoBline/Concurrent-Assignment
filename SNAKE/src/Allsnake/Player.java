package Allsnake;

/**
 * The Player Class
 *
 */
public class Player {
    private String ID;
    private int myScore;
    private Snake mySnake;
    private Buffer playerBuffer;



    /**
     * Move the Snake on the map from what player press on the keyboard
     *
     * @param move The 'move' that get from player
     */
    public void getPlayerMove(char move){
        playerBuffer.append(move);
    }



    public void InitSnake(){
    	mySnake = new Snake(array)

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
