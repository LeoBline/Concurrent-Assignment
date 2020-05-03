package Allsnake;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;

import UI.GamePanel;


public class Server implements KeyListener, WindowListener {
	//make Server Class singleton
	private static Server server = new Server();

	//Server class will use map singleton
	Map map =  Map.getMap();

	// GRID CONTENT
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MALUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;

	// direction numbers
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;



	private int height = 600;//height of window
	private int width = 600;//width of window
	public static int gameSize = 40; //40*40 nodes on the game window
	public long speed = 70;
	private JFrame frame = new JFrame();
	private GamePanel gamePanel = new GamePanel();
	private Canvas canvas = new Canvas();
	private Graphics graph = null;
	private BufferStrategy strategy = null;

	//to see if the game is over, ture: game is over, false: game is not over
	private boolean game_over = false;
	boolean paused = false;

	private int seconde, minute, milliseconde = 0; // Clock values
	private long cycleTime = 0;
	private long sleepTime = 0;
	private int bonusTime = 0;
	private int malusTime = 0;

	//snakes
	Snake snake1 = null;
	Snake snake2 = null;

	public static void main(String[] args) {
		try {
			server = new Server();
		}catch(Exception e) {
			e.getStackTrace();
		}

		server.init();
		server.mainLoop();
	}

	private void mainLoop() {
		while (!game_over) {
			server.setCycleTime(System.currentTimeMillis());
			if (!server.paused) {
				snake1.setDirection(snake1.getNext_direction());
				snake1.moveSnake();
			}
			server.renderGame();
			server.setCycleTime(System.currentTimeMillis() - server.getCycleTime());
			server.setSleepTime(server.speed - server.getCycleTime());
			if (server.getSleepTime() < 0)
				server.setSleepTime(0);
			try {
				Thread.sleep(server.getSleepTime());
			} catch (InterruptedException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private Server() {
		frame = new JFrame();
		canvas = new Canvas();
		map.setMap(new int[gameSize][gameSize]);//init location on the map
		snake1 = new Snake(new int[gameSize * gameSize][2]);//TODO init new snake, here only have one snake now
		//		snake2 = new Snake(new int[gameSize * gameSize][2]);
	}

	public static Server getSever() {
		return server;
	}
	
	// TODO graphic 
	private void init() {
		//draw background of the game
		frame.setSize(width + 7, height + 300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 7, height + 27);
		canvas.setLocation(0, 50);
		frame.add(canvas);
		frame.add(gamePanel);
		canvas.addKeyListener(this);
		frame.addWindowListener(this);
		frame.dispose();
		frame.validate();
		frame.setTitle("Snake");
		frame.setVisible(true);
		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.WHITE);
		
		frame.setVisible(true);
		
		//strategy is a BufferStategy object in AWT, it is use for draw dynamic parts on canvas
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		graph = strategy.getDrawGraphics();
		
		//two method repersent the game
		initGame();
		renderGame();
	}

	private void initGame() {
		// Initialise tabs
		for (int i = 0; i < gameSize; i++) {
			for (int j = 0; j < gameSize; j++) {
				map.setMapInfo(i, j, Server.EMPTY);
			}
		}
		//initial snake body position
		for (int i = 0; i < gameSize * gameSize; i++) {
			snake1.setSnakeInfo(i, 0, -1);
			snake1.setSnakeInfo(i, 1, -1);
		}
		//initial snake head position
		snake1.setSnakeInfo(0, 0, gameSize / 2);
		snake1.setSnakeInfo(0, 1, gameSize / 2);

		//set snake head first exist in the middle of the map
		map.setMapInfo(gameSize / 2, gameSize / 2, SNAKE);

		//place a random food
		placeBonus(FOOD_BONUS);
	}

	/**
	 * draw all element on the game window
	 */
	public void renderGame() {
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
						gridCase = map.getMapInfo(i, j);
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
					graph.drawString("YOUR SCORE : " + snake1.getScore(), height / 2 - 40, height / 2 +
							50);
					graph.drawString("YOUR TIME : " + getTime(), height / 2 - 42, height / 2
							+ 100);
				} else if (paused) {
					graph.setColor(Color.RED);
					graph.drawString("PAUSED", height / 2 - 30, height / 2);
				}
				graph.setColor(Color.BLACK);
				graph.drawString("SCORE = " + snake1.getScore(), 10, 20);
				graph.drawString("TIME = " + getTime(), 100, 20); // Clock
				graph.dispose();
			} while (strategy.contentsRestored());

			// Draw image from buffer by BufferStrategy object in AWT
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());
	}





	private String getTime() {
		String temps = new String(minute + ":" + seconde);
		if (snake1.getDirection() < 0 || paused)
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


	//--------------------------------- place food-----------------------------------------------------------
	public void placeBonus(int bonus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (map.getMapInfo(x, y) == EMPTY) {
			map.setMapInfo(x, y, bonus_type);
		} else {
			placeBonus(bonus_type);
		}
	}
	public void placeMalus(int malus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (map.getMapInfo(x, y) == EMPTY) {
			map.setMapInfo(x, y, malus_type);
		} else {
			placeMalus(malus_type);
		}
	}


	//TODO IMPLEMENTED FUNCTIONS ----------right now only for one snake---------------------------

	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		Dimension dim;
		switch (code) {
		//------------------------keyboard press control snake1-----------------------------------
		case KeyEvent.VK_UP:
			if (snake1.getDirection() != DOWN) {
				snake1.setNext_direction(UP);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (snake1.getDirection() != UP) {
				snake1.setNext_direction(DOWN);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (snake1.getDirection() != RIGHT) {
				snake1.setNext_direction(LEFT);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (snake1.getDirection() != LEFT) {
				snake1.setNext_direction(RIGHT);
			}
			break;
			//-----------------------keyboard press control snake2--------------------------------------------------
		case KeyEvent.VK_W:
			if (snake2.getDirection() != DOWN) {
				snake2.setNext_direction(UP);
			}
			break;
		case KeyEvent.VK_S:
			if (snake2.getDirection() != UP) {
				snake2.setNext_direction(DOWN);
			}
			break;
		case KeyEvent.VK_A:
			if (snake2.getDirection() != RIGHT) {
				snake2.setNext_direction(LEFT);
			}
			break;
		case KeyEvent.VK_D:
			if (snake1.getDirection() != LEFT) {
				snake1.setNext_direction(RIGHT);
			}
			break;
			//------------------other keyboard press----------------------------------------------------------
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

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public static int getGameSize() {
		return gameSize;
	}

	public static void setGameSize(int gameSize) {
		Server.gameSize = gameSize;
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