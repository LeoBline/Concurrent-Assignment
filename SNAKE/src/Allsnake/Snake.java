package Allsnake;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Snake {
	// direction numbers
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;
	
	//states of each unit on map
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MALUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;
	
	//
	private int gameSize = 80;
	public int bonusTime = 0;
	public int malusTime = 0;

	// two singleton
	Map map =  Map.getMap();
	
	// represent the body position: Xposition[i][0], Yposition[i][1]
	private int[][] snake = null;
	
	//current direction
	private int direction = -1;
	private int next_direction = -1;

	//to show if the snake is dead, true:snake is dead, false: snake is alive
	private boolean game_over = false;
	private int score = 0;
	private int Length = 0;//length of the snake

	/**
	 * constructor
	 * @param array snake body
	 */
	public Snake(int[][] array) {
		snake = array;//init new snake body
	}
	
	/**
	 * Each time snake move, this method will adjust attributes behind UI
	 */
	public synchronized void moveSnake() {
		if(this.getSnakeInfo(0, 0)==-1) {//snake doesn't exist
			return;
		}
		if (direction < 0) {//no direction
			return;
		}
		int ymove = 0;
		int xmove = 0;
		switch (direction) {
		case UP:
			xmove = 0;
			ymove = -1;
			break;
		case DOWN:
			xmove = 0;
			ymove = 1;
			break;
		case RIGHT:
			xmove = 1;
			ymove = 0;
			break;
		case LEFT:
			xmove = -1;
			ymove = 0;
			break;
		default:
			xmove = 0;
			ymove = 0;
			break;
		}
		//get snake head's position in tempx and tempy
		int tempx = this.getSnakeInfo(0, 0);
		int tempy = this.getSnakeInfo(0, 1);
		//get snake head's position after moving
		int fut_x = this.getSnakeInfo(0, 0) + xmove;
		int fut_y = this.getSnakeInfo(0, 1) + ymove;
		if (fut_x < 0)// snake head exceeds the left border of the map
			fut_x = gameSize - 1;
		if (fut_y < 0)// snake head exceeds the top border of the map
			fut_y = gameSize - 1;
		if (fut_x >= gameSize)// snake head exceeds the right border of the map
			fut_x = 0;
		if (fut_y >= gameSize)// snake head exceeds the down border of the map
			fut_y = 0;
		// if snake head is on the food, adjust the snake length and score
		if (map.getMapInfo(fut_x, fut_y)== FOOD_BONUS) {
			Length++;
			score++;
			placeBonus(FOOD_BONUS);
		}
		if (map.getMapInfo(fut_x, fut_y) == FOOD_MALUS) {
			Length += 2;
			score--;
		} else if (map.getMapInfo(fut_x, fut_y) == BIG_FOOD_BONUS) {
			Length += 3;
			score += 3;
		}
		if ((map.getMapInfo(fut_x, fut_y) == SNAKE)) {
			gameOver();

			return;
		}
		//save snake head position in the snake array
		this.setSnakeInfo(0, 0, fut_x);
		this.setSnakeInfo(0, 1, fut_y);
		
		//set snake head's last position into empty, ready for snake body move on
		map.setMapInfo(tempx, tempy, EMPTY);
		int snakex, snakey, i;
		for (i = 1; i < gameSize * gameSize; i++) {
			if ((this.getSnakeInfo(i, 0) < 0) || (this.getSnakeInfo(i, 0) < 0)) {
				break;
			}
			map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), EMPTY);
			snakex = this.getSnakeInfo(i, 0);
			snakey = this.getSnakeInfo(i, 1);
			this.setSnakeInfo(i, 0, tempx);
			this.setSnakeInfo(i, 1, tempy);
			tempx = snakex;
			tempy = snakey;
		}
		for (i = 0; i < gameSize * gameSize; i++) {
			if ((this.getSnakeInfo(i, 0) < 0) || (this.getSnakeInfo(i, 1) < 0)) {
				break;
			}
			map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), SNAKE);
		}
		bonusTime = bonusTime -1;
		if (bonusTime == 0) {
			for (i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					if (map.getMapInfo(i, j)== BIG_FOOD_BONUS)
						map.setMapInfo(i, j, EMPTY);
				}
			}
		}
		malusTime = malusTime-1;
		if (malusTime == 0) {
			for (i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					if (map.getMapInfo(i, j) == FOOD_MALUS)
						map.setMapInfo(i, j, EMPTY);
				}
			}
			}
		if (Length > 0) {
			this.setSnakeInfo(i, 0, tempx);
			this.setSnakeInfo(i, 1, tempx);
			map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), SNAKE);
			if (score % 10 == 0) {
				placeBonus(BIG_FOOD_BONUS);
				bonusTime = 100;
			}
			if (score % 5 == 0) {
				placeMalus(FOOD_MALUS);
				malusTime = 100;
			}
			Length--;
		}
	}
	
	/**
	 * gameOver means this snake is dead
	 */
	public void gameOver() {
		game_over = true;
		for(int i = 0 ; i<  gameSize * gameSize;i++) {
			if(this.getSnakeInfo(i, 0)>-1) {
				map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), EMPTY);
				this.setSnakeInfo(i, 0, -1);
				this.setSnakeInfo(i, 1, -1);
			}
		}
	}
	
	/**
	 * Return the statement of this snake, if the snake dead or alive
	 * @return
	 */
	public boolean getGameover() {
		return game_over;
	}

	public void placeBonus(int bonus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (map.getMapInfo(x, y) == EMPTY) {
			map.setMapInfo(x, y,bonus_type);
		} else {
			placeBonus(bonus_type);
		}
	}
	public void placeMalus(int malus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (map.getMapInfo(x, y) == EMPTY) {
			map.setMapInfo(x, y,malus_type);
		} else {
			placeMalus(malus_type);
		}
	}
	/**
	 * get random direction for snake to move.
	 * this method return a random direction
	 * @return direction
	 */
	public int RandomDirection() {
		
		Random r=new Random();
		int i=r.nextInt(4);
		return i;
	}

	
	public void setSnakeInfo(int index1, int index2, int num) {
		snake[index1][index2] = num;
	}
	/**
	 * return the location of one part on snake
	 * @param index1
	 * @param index2
	 * @return one part of snake
	 */
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
