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
    private boolean IsRobot = false;
    private int[] OperationButtons={999,999,999,999};//up,down,right,left keypress
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;



    /**
     * Move the Snake on the map from what player press on the keyboard
     *
     * @param move The 'move' that get from player
     */

    public  Player(String id,int gamesize) {
    	this.ID = id;
		game_size=gamesize;
		int[][] a =new int[game_size * game_size][2];
		mySnake = new Snake(a);
		
	}
    public Buffer Getbuffer() {
    	return playerBuffer;
    }


    public synchronized void InitSnake(){
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
    
    public void setkeypress(int up,int down,int right,int left) {
    	OperationButtons[0]=up;
    	OperationButtons[1]=down;
    	OperationButtons[2]=right;
    	OperationButtons[3]=left;
    	
    }
    
    public int getUpButtons() {
    	return OperationButtons[0];
    }
    public int getDownButtons() {
    	return OperationButtons[1];
    }
    public int getRightButtons() {
    	return OperationButtons[2];
    }
    public int getLeftButtons() {
    	return OperationButtons[3];
    }
    
    public void judgeInput(int a) {
    	if(a==this.getUpButtons()) {
    		if(this.getSnake().getDirection()!=DOWN && this.getSnake().getDirection()!=UP) {
    		this.Getbuffer().append(UP);
    		}
    	}
    	if(a==this.getDownButtons()) {
    		if(this.getSnake().getDirection()!=DOWN && this.getSnake().getDirection()!=UP) {
    		this.Getbuffer().append(DOWN);
    		}
    	}
    	if(a==this.getRightButtons()) {
    		if(this.getSnake().getDirection()!=RIGHT && this.getSnake().getDirection()!=LEFT) {
    		this.Getbuffer().append(RIGHT);
    		}
    	}
    	if(a==this.getLeftButtons()) {
    		if(this.getSnake().getDirection()!=RIGHT && this.getSnake().getDirection()!=LEFT) {
    		this.Getbuffer().append(LEFT);
    		}
    	}
    	
    	
    }

//    public void setID(String ID) {
//        this.ID = ID;
//    }


}
