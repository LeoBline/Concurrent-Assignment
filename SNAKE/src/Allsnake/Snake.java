package Allsnake;

public class Snake {
	// direction numbers
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	//two singleton
	Map map =  Map.getMap();
	Server server = Server.getSever();
	
	private int[][] snake = null;//xposition[i][0], Yposition[i][1]
	private int direction = -1;//current direction
	private int next_direction = -1;

	private int score = 0;
	private int grow = 0;//length of the snake


	public Snake(int[][] array) {
		snake = array;
	}

	public void moveSnake() {
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
		int tempx = snake[0][0];
		int tempy = snake[0][1];
		int fut_x = snake[0][0] + xmove;//head Xposition change
		int fut_y = snake[0][1] + ymove;//head Yposition change
		if (fut_x < 0)
			fut_x = Server.gameSize - 1;
		if (fut_y < 0)
			fut_y = Server.gameSize - 1;
		if (fut_x >= Server.gameSize)
			fut_x = 0;
		if (fut_y >= Server.gameSize)
			fut_y = 0;
		//caculate length after each moving
		if (map.getMapInfo(fut_x, fut_y) == Server.FOOD_BONUS) {
			grow++;
			score++;
			server.placeBonus(Server.FOOD_BONUS);
		}
		if (map.getMapInfo(fut_x, fut_y) == Server.FOOD_MALUS) {
			grow += 2;
			score--;
		} else if (map.getMapInfo(fut_x, fut_y) == Server.BIG_FOOD_BONUS) {
			grow += 3;
			score += 3;
		}
		//move snake head
		snake[0][0] = fut_x;
		snake[0][1] = fut_y;
		//when snake head hit a snake's part, game is over
		if (map.getMapInfo(snake[0][0], snake[0][1]) == Server.SNAKE) {
			server.gameOver();
			return;
		}
		
		map.setMapInfo(tempx, tempy, Server.EMPTY);
		int snakex, snakey, i;
		for (i = 1; i < Server.gameSize * Server.gameSize; i++) {
			if ((snake[i][0] < 0) || (snake[i][1] < 0)) {
				break;
			}
			map.setMapInfo(snake[i][0], snake[i][1], Server.EMPTY);
			snakex = snake[i][0];
			snakey = snake[i][1];
			snake[i][0] = tempx;
			snake[i][1] = tempy;
			tempx = snakex;
			tempy = snakey;
		}
		
		for (i = 0; i < Server.gameSize * Server.gameSize; i++) {
			if ((snake[i][0] < 0) || (snake[i][1] < 0)) {
				break;
			}
			map.setMapInfo(snake[i][0], snake[i][1], Server.SNAKE);
		}
		server.setBonusTime(server.getBonusTime() - 1);
		if (server.getBonusTime() == 0) {
			for (i = 0; i < Server.gameSize; i++) {
				for (int j = 0; j < Server.gameSize; j++) {
					if (map.getMapInfo(i, j) == Server.BIG_FOOD_BONUS)
						map.setMapInfo(i, j, Server.EMPTY);
				}
			}
		}
		server.setMalusTime(server.getMalusTime() - 1);
		if (server.getMalusTime() == 0) {
			for (i = 0; i < Server.gameSize; i++) {
				for (int j = 0; j < Server.gameSize; j++) {
					if (map.getMapInfo(i, j) == Server.FOOD_MALUS)
						map.setMapInfo(i, j, Server.EMPTY);
				}
			}
		}
		if (grow > 0) {
			snake[i][0] = tempx;
			snake[i][1] = tempy;
			map.setMapInfo(snake[i][0], snake[i][1], Server.SNAKE);
			if (score % 10 == 0) {
				server.placeBonus(Server.BIG_FOOD_BONUS);
				server.setBonusTime(100);
			}
			if (score % 5 == 0) {
				server.placeMalus(Server.FOOD_MALUS);
				server.setMalusTime(100);
			}
			grow--;
		}
	}

	public void setSnakeInfo(int index1, int index2, int num) {
		snake[index1][index2] = num;
	}
	
	public int getSnakeInfo(int index1, int index2) {
		return snake[index1][index2];
	}
}
