package Allsnake;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Snake {
	// direction numbers
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MALUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;
	private int gameSize = 80;
	public int bonusTime = 0;
	public int malusTime = 0;

	//two singleton
	Map map =  Map.getMap();
//	ServerUIControl server = ServerUIControl.getSever();
	
	private int[][] snake = null;//xposition[i][0], Yposition[i][1]
	private int direction = -1;//current direction
	private int next_direction = -1;

	private boolean game_over = false;//to show if the snake is dead
	private int score = 0;
	private int Length = 0;//length of the snake


	public Snake(int[][] array) {
		snake = array;//init new snake body
	}


	
	public void setSnakeInfo(int index1, int index2, int num) {
		snake[index1][index2] = num;
	}
	
	public int getSnakeInfo(int index1, int index2) {
		return snake[index1][index2];
	}
		
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getNext_direction() {
		return next_direction;
	}

	public void setNext_direction(int next_direction) {
		this.next_direction = next_direction;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
