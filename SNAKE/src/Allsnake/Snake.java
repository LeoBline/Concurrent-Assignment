package Allsnake;

public class Snake {
	public Snake() {
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
			int fut_x = snake[0][0] + xmove;
			int fut_y = snake[0][1] + ymove;
			if (fut_x < 0)
				fut_x = gameSize - 1;
			if (fut_y < 0)
				fut_y = gameSize - 1;
			if (fut_x >= gameSize)
				fut_x = 0;
			if (fut_y >= gameSize)
				fut_y = 0;
			if (grid[fut_x][fut_y] == FOOD_BONUS) {
				grow++;
				score++;
				placeBonus(FOOD_BONUS);
			}
			if (grid[fut_x][fut_y] == FOOD_MALUS) {
				grow += 2;
				score--;
			} else if (grid[fut_x][fut_y] == BIG_FOOD_BONUS) {
				grow += 3;
				score += 3;
			}
			snake[0][0] = fut_x;
			snake[0][1] = fut_y;
			if ((grid[snake[0][0]][snake[0][1]] == SNAKE)) {
				gameOver();
				return;
			}
			grid[tempx][tempy] = EMPTY;
			int snakex, snakey, i;
			for (i = 1; i < gameSize * gameSize; i++) {
				if ((snake[i][0] < 0) || (snake[i][1] < 0)) {
					break;
				}
				grid[snake[i][0]][snake[i][1]] = EMPTY;
				snakex = snake[i][0];
				snakey = snake[i][1];
				snake[i][0] = tempx;
				snake[i][1] = tempy;
				tempx = snakex;
				tempy = snakey;
			}
			for (i = 0; i < gameSize * gameSize; i++) {
				if ((snake[i][0] < 0) || (snake[i][1] < 0)) {
					break;
				}
				grid[snake[i][0]][snake[i][1]] = SNAKE;
			}
			bonusTime--;
			if (bonusTime == 0) {
				for (i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						if (grid[i][j] == BIG_FOOD_BONUS)
							grid[i][j] = EMPTY;
					}
				}
			}
			malusTime--;
			if (malusTime == 0) {
				for (i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						if (grid[i][j] == FOOD_MALUS)
							grid[i][j] = EMPTY;
					}
				}
			}
			if (grow > 0) {
				snake[i][0] = tempx;
				snake[i][1] = tempy;
				grid[snake[i][0]][snake[i][1]] = SNAKE;
				if (score % 10 == 0) {
					placeBonus(BIG_FOOD_BONUS);
					bonusTime = 100;
				}
				if (score % 5 == 0) {
					placeMalus(FOOD_MALUS);
					malusTime = 100;
				}
				grow--;
			}
		}
	}
}
