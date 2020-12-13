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
	 *
	 * @param id
	 * @param gameSize
	 */
	public Player(String id,int gameSize) {
    	this.ID = id;
		game_size=gameSize;
		int[][] a =new int[game_size * game_size][2];
		mySnake = new Snake(a);
	}

    public synchronized void InitSnake(){
    	mySnake = new Snake(new int[game_size * game_size][2]);
    	playerBuffer = new Buffer();
    }


    public void setKeypress(int up, int down, int right, int left) {
    	OperationButtons[0]=up;
    	OperationButtons[1]=down;
    	OperationButtons[2]=right;
    	OperationButtons[3]=left;
    }
	//Getter and setter
	public int getScore() {
		myScore = mySnake.getScore();
		return myScore;
	}
	public String getID() {
		return ID;
	}
	public Buffer getPlayerBuffer() { return playerBuffer; }
	public void setIsRobot(boolean flag) {
		this.IsRobot = flag;
	}
	public boolean getIsRobot() {
		return this.IsRobot;
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
	public synchronized Snake getSnake() {
		return mySnake;
	}

    public void judgeInput(int a) {
    	if(a==getUpButtons()) {
    		if(getSnake().getDirection()!=DOWN && getSnake().getDirection()!=UP) {
    		getPlayerBuffer().append(UP);
    		}
    	}
    	if(a==getDownButtons()) {
    		if(getSnake().getDirection()!=DOWN && getSnake().getDirection()!=UP) {
    		getPlayerBuffer().append(DOWN);
    		}
    	}
    	if(a==getRightButtons()) {
    		if(getSnake().getDirection()!=RIGHT && getSnake().getDirection()!=LEFT) {
    		getPlayerBuffer().append(RIGHT);
    		}
    	}
    	if(a==getLeftButtons()) {
    		if(getSnake().getDirection()!=RIGHT && getSnake().getDirection()!=LEFT) {
    		getPlayerBuffer().append(LEFT);
    		}
    	}
    	
    	
    }

}
