package Allsnake;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;


public class Server implements KeyListener, WindowListener {
	//make Server Class singleton
	private static Server server = null;

	// GRID CONTENT
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MALUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;

	Map map =  Map.getMap();

	private int height = 600;
	private int width = 600;
	public static int gameSize = 40;
	private long speed = 70;
	private JFrame frame = null;
	private Canvas canvas = null;
	private Graphics graph = null;
	private BufferStrategy strategy = null;
	//to see if the game is over, ture: game is over, false: game is not over
	private boolean game_over = false;
	private boolean paused = false;

	private int seconde, minute, milliseconde = 0; // Clock values
	private long cycleTime = 0;
	private long sleepTime = 0;
	private int bonusTime = 0;
	private int malusTime = 0;

	public static void main(String[] args) {
		Server game = new Server();
		game.init();
		game.mainLoop();
	}

	private Server() {
		frame = new JFrame();
		canvas = new Canvas();
		map.setMap(new int[gameSize][gameSize]);//init location on the map
		Snake snake = new Snake(int[gameSize * gameSize][2]);//TODO init new snake, here only have one snake now
	}

	public static Server getSever() {
		if(server == null) {
			server = new Server();
		}
		return server;
	}
	private void init() {
		frame.setSize(width + 7, height + 27);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 7, height + 27);
		frame.add(canvas);
		canvas.addKeyListener(this);
		frame.addWindowListener(this);
		frame.dispose();
		frame.validate();
		frame.setTitle("Snake");
		frame.setVisible(true);
		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.WHITE);
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		graph = strategy.getDrawGraphics();
		initGame();
		renderGame();
	}

	private void initGame() {
		// Initialise tabs
		for (int i = 0; i < gameSize; i++) {
			for (int j = 0; j < gameSize; j++) {
				grid[i][j] = EMPTY;
			}
		}
		for (int i = 0; i < gameSize * gameSize; i++) {
			snake[i][0] = -1;
			snake[i][1] = -1;
		}
		snake[0][0] = gameSize / 2;
		snake[0][1] = gameSize / 2;
		grid[gameSize / 2][gameSize / 2] = SNAKE;
		placeBonus(FOOD_BONUS);
	}

	private void renderGame() {
		int gridUnit = height / gameSize;
		canvas.paint(graph);
		do {
			do {
				graph = strategy.getDrawGraphics();
				// Draw Background
				graph.setColor(Color.WHITE);
				graph.fillRect(0, 0, width, height);
				// Draw snake, bonus ...
				int gridCase = EMPTY;
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						gridCase = grid[i][j];
						switch (gridCase) {
						case SNAKE:
							graph.setColor(Color.BLUE);
							graph.fillOval(i * gridUnit, j * gridUnit, gridUnit, gridUnit);
							break;
						case FOOD_BONUS:
							graph.setColor(Color.darkGray);
							graph.fillOval(i * gridUnit + gridUnit / 4, j * gridUnit +
									gridUnit / 4, gridUnit / 2,
									gridUnit / 2);
							break;
						case FOOD_MALUS:
							graph.setColor(Color.RED);
							graph.fillOval(i * gridUnit + gridUnit / 4, j * gridUnit +
									gridUnit / 4, gridUnit / 2,
									gridUnit / 2);
							break;
						case BIG_FOOD_BONUS:
							graph.setColor(Color.GREEN);
							graph.fillOval(i * gridUnit + gridUnit / 4, j * gridUnit +
									gridUnit / 4, gridUnit / 2,
									gridUnit / 2);
							break;
						default:
							break;
						}
					}
				}
				graph.setFont(new Font(Font.SANS_SERIF, Font.BOLD, height / 40));
				if (game_over) {
					graph.setColor(Color.RED);
					graph.drawString("GAME OVER", height / 2 - 30, height / 2);
					graph.drawString("YOUR SCORE : " + score, height / 2 - 40, height / 2 +
							50);
					graph.drawString("YOUR TIME : " + getTime(), height / 2 - 42, height / 2
							+ 100);
				} else if (paused) {
					graph.setColor(Color.RED);
					graph.drawString("PAUSED", height / 2 - 30, height / 2);
				}
				graph.setColor(Color.BLACK);
				graph.drawString("SCORE = " + score, 10, 20);
				graph.drawString("TIME = " + getTime(), 100, 20); // Clock
				graph.dispose();
			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());
	}

	private void mainLoop() {
		while (!game_over) {
			cycleTime = System.currentTimeMillis();
			if (!paused) {
				direction = next_direction;
				moveSnake();
			}
			renderGame();
			cycleTime = System.currentTimeMillis() - cycleTime;
			sleepTime = speed - cycleTime;
			if (sleepTime < 0)
				sleepTime = 0;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private String getTime() {
		String temps = new String(minute + ":" + seconde);
		if (direction < 0 || paused)
			return temps;
		milliseconde++;
		if (milliseconde == 14) {
			seconde++;
			milliseconde = 0;
		}
		if (seconde == 60) {
			seconde = 0;
			minute++;
		}
		return temps;
	}

	private void moveSnake() {
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

	public void placeBonus(int bonus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (grid[x][y] == EMPTY) {
			grid[x][y] = bonus_type;
		} else {
			placeBonus(bonus_type);
		}
	}
	public void placeMalus(int malus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (grid[x][y] == EMPTY) {
			grid[x][y] = malus_type;
		} else {
			placeMalus(malus_type);
		}
	}
	
	public void gameOver() {
		game_over = true;
	}

	//TODO IMPLEMENTED FUNCTIONS ----------right now only for one snake---------------------------

	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		Dimension dim;
		switch (code) {
		case KeyEvent.VK_UP:
			if (direction != DOWN) {
				next_direction = UP;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (direction != UP) {
				next_direction = DOWN;
			}
			break;
		case KeyEvent.VK_LEFT:
			if (direction != RIGHT) {
				next_direction = LEFT;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (direction != LEFT) {
				next_direction = RIGHT;
			}
			break;
		case KeyEvent.VK_F11:
			dim = Toolkit.getDefaultToolkit().getScreenSize();
			if ((height != dim.height - 50) || (width != dim.height - 50)) {
				height = dim.height - 50;
				width = dim.height - 50;
			} else {
				height = 600;
				width = 600;
			}
			frame.setSize(width + 7, height + 27);
			canvas.setSize(width + 7, height + 27);
			canvas.validate();
			frame.validate();
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_SPACE:
			if (!game_over)
				paused = !paused;
			break;
		default:
			// Unsupported key
			break;
		}
	}
	
	//-----------setter-----getter-------------------------------------------------------------
	
	public int getSeconde() {
		return seconde;
	}

	public void setSeconde(int seconde) {
		this.seconde = seconde;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getMilliseconde() {
		return milliseconde;
	}

	public void setMilliseconde(int milliseconde) {
		this.milliseconde = milliseconde;
	}

	public long getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(long cycleTime) {
		this.cycleTime = cycleTime;
	}

	public int getBonusTime() {
		return bonusTime;
	}

	public void setBonusTime(int bonusTime) {
		this.bonusTime = bonusTime;
	}

	public int getMalusTime() {
		return malusTime;
	}

	public void setMalusTime(int malusTime) {
		this.malusTime = malusTime;
	}

	//--------------------------------UNNUSED IMPLEMENTED FUNCTIONS--------------------------------------------
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}