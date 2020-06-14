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
	
	//states of each unit on map
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MINUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;
	
	//  singleton: map object
	Map map =  Map.getMap();
	
	public int bonusTime = 0;
	public int malusTime = 0;
	private int gameSize = map.getGameSize();
	
	

	// represent the body position: Xposition[i][0], Yposition[i][1]
	private int[][] snake = null;
	
	//current direction
	private int direction = -1;
	private int next_direction = -1;

	//to show if the snake is dead, true:snake is dead, false: snake is alive
	private boolean snakeDied = false;

	// score of the snake
	private int score = 0;
	
	//length of the snake
	private int Length = 0;

	/**
	 * constructor
	 * @param array snake body
	 */
	public Snake(int[][] array) {
		snake = array;
	}
	
	/**
	 *How snake move on Grid
	 */
	public void moveSnake() {
		if(getSnakeInfo(0, 0) == -1) {//whether snake is dead
			gameOver();
			return;
		}
		if (direction < 0) {//no direction
			return;
		}
		int yMove;
		int xMove;
		switch (direction) {
			case UP:
				xMove = 0;
				yMove = -1;
				break;
			case DOWN:
				xMove = 0;
				yMove = 1;
				break;
			case RIGHT:
				xMove = 1;
				yMove = 0;
				break;
			case LEFT:
				xMove = -1;
				yMove = 0;
				break;
			default:
				xMove = 0;
				yMove = 0;
				break;
		}

		//get snake head's position in tempx and tempy
		int tempX = this.getSnakeInfo(0, 0);
		int tempY = this.getSnakeInfo(0, 1);
		//get snake head's position after moving
		int fut_x = this.getSnakeInfo(0, 0) + xMove;
		int fut_y = this.getSnakeInfo(0, 1) + yMove;
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
			//if two snake eat same food it will stop one
//			System.out.println("Eat");

			if(map.eatFood(fut_x,fut_y)==true) {
//				System.out.println("Eat");
				setLength(Length+1);
				setScore(score+1);
			placeBonus(FOOD_BONUS);
			}
		}
		if (map.getMapInfo(fut_x, fut_y) == FOOD_MINUS) {
			//if two snake eat same food it will stop one
			if(map.eatFood(fut_x,fut_y)==true) {
				setLength(Length+2);
				setScore(score-1);

			}
		} else if (map.getMapInfo(fut_x, fut_y) == BIG_FOOD_BONUS) {
			//if two snake eat same food it will stop one
			if(map.eatFood(fut_x,fut_y)==true) {
			setLength(Length+3);
			setScore(score+3);
			}
		}
		if ((map.getMapInfo(fut_x, fut_y) == SNAKE)) {
			
			gameOver();

			return;
		}
		//save snake head position in the snake array
		this.setSnakeInfo(0, 0, fut_x);
		this.setSnakeInfo(0, 1, fut_y);

		//set snake head's last position into empty, ready for snake body move on
		map.setMapInfo(tempX, tempY, EMPTY);

		updateSnake(tempX, tempY);

		int i;
		for ( i = 0; i < gameSize*gameSize; i++) {
			if ((this.getSnakeInfo(i, 0) < 0) || (this.getSnakeInfo(i, 1) < 0)) {
				break;
			}
			
			
		}
		setBonusTime(bonusTime-1);
		if (getBonusTime() == 0) {
			for (i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					if (map.getMapInfo(i, j)== BIG_FOOD_BONUS)
						map.setMapInfo(i, j, EMPTY);
				}
			}
		}
		setMalusTime(malusTime-1);
		if (getMalusTime() == 0) {
			for (i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					if (map.getMapInfo(i, j) == FOOD_MINUS)
						map.setMapInfo(i, j, EMPTY);
				}
			}
			}
		if (getLength() > 0) {
			this.setSnakeInfo(i, 0, tempX);
			this.setSnakeInfo(i, 1, tempX);
			map.setMapInfo(this.getSnakeInfo(i, 0), this.getSnakeInfo(i, 1), SNAKE);
			if (getScore() % 10 == 0) {
				placeBonus(BIG_FOOD_BONUS);
				setBonusTime(100);
			}
			if (getScore() % 5 == 0) {
				placeMinus(FOOD_MINUS);
				setMalusTime(100);
			}
			setLength(Length-1);
		}
	}
	
	/**
	 * gameOver means this snake is dead,this will initialize the snake's infor
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

	/**
	 * Place a random bonus on map
	 * @param bonus_type
	 */
	public void placeBonus(int bonus_type) {
		//Create random location
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (map.getMapInfo(x, y) == EMPTY) {
			map.setMapInfo(x, y,bonus_type);
		} else {
			placeBonus(bonus_type);
		}
	}
	public void placeBonus(int x,int y) {
		//Create random location


			map.setMapInfo(x, y,FOOD_BONUS);

	}

	/**
	 * Place a random minus on map
	 * @param malus_type
	 */
	public void placeMinus(int malus_type) {
		//Create random location
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (map.getMapInfo(x, y) == EMPTY) {
			map.setMapInfo(x, y,malus_type);
		} else {
			placeMinus(malus_type);
		}
	}

	/**
	 * get random direction for robot to move
	 * @return random direction
	 */
	public synchronized int RandomDirection() {
		Random r=new Random();
		int i=r.nextInt(4);
		return i;
	}

	/**
	 * Update the Snake's length and shape
	 * @param tempx
	 * @param tempy
	 */
	public synchronized void updateSnake(int tempx, int tempy) {
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

	/**
	 * return the real length of Snake,because the length is just temp variable
	 * @return
	 */
	public int getRealLength(){
		int length = 0;
		for(int i = 0; i < snake.length; i++){
			if(snake[i][1] != -1){
				length += 1;
			}
		}
		return length;
	}

	//Getters and setters
	public synchronized int[][] setSnakearray() {
		return this.snake;
	}
	public synchronized void setSnakeInfo(int index1, int index2, int num) {
		snake[index1][index2] = num;
	}
	public synchronized int getSnakeInfo(int index1, int index2) { return snake[index1][index2]; }
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
	public synchronized int getBonusTime() {
		return bonusTime;
	}
	public synchronized void setBonusTime(int bonusTime) {
		this.bonusTime = bonusTime;
	}
	public synchronized int getMalusTime() {
		return malusTime;
	}
	public synchronized void setMalusTime(int malusTime) {
		this.malusTime = malusTime;
	}
	
}
