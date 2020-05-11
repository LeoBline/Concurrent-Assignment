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

	public synchronized void moveSnake() {
		if (direction < 0) {
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
		int tempx = this.getSnakeInfo(0, 0);
		int tempy = this.getSnakeInfo(0, 1);
		int fut_x = this.getSnakeInfo(0, 0) + xmove;
		int fut_y = this.getSnakeInfo(0, 1) + ymove;
		if (fut_x < 0)
			fut_x = gameSize - 1;
		if (fut_y < 0)
			fut_y = gameSize - 1;
		if (fut_x >= gameSize)
			fut_x = 0;
		if (fut_y >= gameSize)
			fut_y = 0;
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
		this.setSnakeInfo(0, 0, fut_x);
		this.setSnakeInfo(0, 1, fut_y);
		if ((map.getMapInfo(this.getSnakeInfo(0, 0), this.getSnakeInfo(0, 1)) == SNAKE)) {
			gameOver();
			return;
		}
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
		
	}
	public synchronized void moveSnake() {
		if (direction < 0) {
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
		int tempx = this.getSnakeInfo(0, 0);
		int tempy = this.getSnakeInfo(0, 1);
		int fut_x = this.getSnakeInfo(0, 0) + xmove;
		int fut_y = this.getSnakeInfo(0, 1) + ymove;
		if (fut_x < 0)
			fut_x = gameSize - 1;
		if (fut_y < 0)
			fut_y = gameSize - 1;
		if (fut_x >= gameSize)
			fut_x = 0;
		if (fut_y >= gameSize)
			fut_y = 0;
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
		this.setSnakeInfo(0, 0, fut_x);
		this.setSnakeInfo(0, 1, fut_y);
		if ((map.getMapInfo(this.getSnakeInfo(0, 0), this.getSnakeInfo(0, 1)) == SNAKE)) {
			gameOver();
			return;
		}
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
