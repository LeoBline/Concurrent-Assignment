package Allsnake;

import java.util.Random;

/**
 * Snake class refer to a snake that player will play in the game
 */
public class Snake {

	// Directions
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MALUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;
	private int gameSize = 100;

	public int bonusTime = 0;
	public int malusTime = 0;

	Map map =  Map.getMap();
	
	private int[][] snake = null;
	private int direction = -1;
	private int next_direction = -1;

	private boolean snakeDied = false;
	private int score = 0;
	private int Length = 0;


	public Snake(int[][] array) {
		snake = array;//init new snake body
	}
	
	/**
	 * Each time snake move, this method will adjust attributes behind UI
	 */
	public void moveSnake() {
		if(this.getSnakeInfo(0, 0)==-1) {
			this.gameOver();
			return;
		}
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
			//if two snake eat same food it will stop one
			if(map.eatfood(fut_x,fut_y)==true) {
				setLength(Length+1);
				setScore(score+1);
			placeBonus(FOOD_BONUS);
			}
		}
		if (map.getMapInfo(fut_x, fut_y) == FOOD_MALUS) {
			//if two snake eat same food it will stop one
			if(map.eatfood(fut_x,fut_y)==true) {
				setLength(Length+2);
				setScore(score-1);

			}
		} else if (map.getMapInfo(fut_x, fut_y) == BIG_FOOD_BONUS) {
			//if two snake eat same food it will stop one
			if(map.eatfood(fut_x,fut_y)==true) {
			setLength(Length+3);
			setScore(score+3);
			}
		}
		if ((map.getMapInfo(fut_x, fut_y) == SNAKE)) {
			
			gameOver();

			return;
		}
		this.setSnakeInfo(0, 0, fut_x);
		this.setSnakeInfo(0, 1, fut_y);

		map.setMapInfo(tempx, tempy, EMPTY);

		updatasnake(tempx, tempy);
		int i;
		for ( i = 0; i < gameSize*gameSize; i++) {
			if ((this.getSnakeInfo(i, 0) < 0) || (this.getSnakeInfo(i, 1) < 0)) {
				break;
			}
			
			
		}
		bonusTime = bonusTime -1;
		if (getBonusTime() == 0) {
			for (i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					if (map.getMapInfo(i, j)== BIG_FOOD_BONUS)
						map.setMapInfo(i, j, EMPTY);
				}
			}
		}
		malusTime = malusTime-1;
//		System.out.println(malusTime);
		if (getMalusTime() == 0) {
			for (i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					if (map.getMapInfo(i, j) == FOOD_MALUS)
						map.setMapInfo(i, j, EMPTY);
				}
			}
			}
		if (getLength() > 0) {
			this.setSnakeInfo(i, 0, tempx);
			this.setSnakeInfo(i, 1, tempx);
			map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), SNAKE);
			if (getScore() % 10 == 0) {
				placeBonus(BIG_FOOD_BONUS);
				setBonusTime(100);
			}
			if (getScore() % 5 == 0) {
				placeMalus(FOOD_MALUS);
				setMalusTime(100);
			}
			setLength(Length-1);
		}
	}
	
	/**
	 * gameOver means this snake is dead
	 */
	public synchronized void gameOver() {
		snakeDied = true;
		for(int i = 0 ; i<  gameSize * gameSize;i++) {
			if(this.getSnakeInfo(i, 0)>-1) {
				map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), EMPTY);
				int a =this.getSnakeInfo(i, 0);;
				int b = this.getSnakeInfo(i, 1);
				this.setSnakeInfo(i, 0, -1);
				this.setSnakeInfo(i, 1, -1);
				map.setMapInfo(a, b, EMPTY);
			}
		}
	}
	
	/**
	 * Return the statement of this snake, if the snake dead or alive
	 * @return
	 */
	public synchronized boolean getGameover() {
		return snakeDied;
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
	public synchronized int RandomDirection() {
		
		Random r=new Random();
		int i=r.nextInt(4);
		return i;
	}

	public synchronized void updatasnake(int tempx,int tempy) {
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
		for ( i = 0; i < gameSize*gameSize; i++) {
			if ((this.getSnakeInfo(i, 0) < 0) || (this.getSnakeInfo(i, 1) < 0)) {
				break;
			}
			
			map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), SNAKE);
			
		}
	}
	
	public synchronized void setSnakeInfo(int index1, int index2, int num) {
		snake[index1][index2] = num;
	}
	/**
	 * return the location of one part on snake
	 * @param index1
	 * @param index2
	 * @return one part of snake
	 */
	public synchronized int getSnakeInfo(int index1, int index2) {
		return snake[index1][index2];
	}
		
	public synchronized int getDirection() {
		return direction;
	}

	public synchronized void setDirection(int direction) {
		this.direction = direction;
	}
	
	public synchronized int getNext_direction() {
		return next_direction;
	}

	public synchronized void setNext_direction(int next_direction) {
		this.next_direction = next_direction;
	}

	public synchronized int getScore() {
		return score;
	}

	public synchronized int getLength() {
		return Length;
	}
	public synchronized void setLength(int length) {
		this.Length = length;
	}
	public synchronized void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the bonusTime
	 */
	public synchronized int getBonusTime() {
		return bonusTime;
	}

	/**
	 * @param bonusTime the bonusTime to set
	 */
	public synchronized void setBonusTime(int bonusTime) {
		this.bonusTime = bonusTime;
	}

	/**
	 * @return the malusTime
	 */
	public synchronized int getMalusTime() {
		return malusTime;
	}

	/**
	 * @param malusTime the malusTime to set
	 */
	public synchronized void setMalusTime(int malusTime) {
		this.malusTime = malusTime;
	}
	
}
