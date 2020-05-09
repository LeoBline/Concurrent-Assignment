/**
 * 
 */
package Allsnake;


//Simple snake code from https://code.google.com/p/java-snake/source/browse/trunk/javasnake/src/snake/Main.java
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
* @author Peuch
*/
public class ServerUIControl extends Thread implements  KeyListener, WindowListener {
	// KEYS MAP
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;
	// GRID CONTENT
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MALUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;
	private int[][] grid = null;
	private int[][] snake = null;
	private int direction = -1;
	private int next_direction = -1;
	private int height = 720;
	private int width = 1200;
	private int gameSize = 80;
	private long speed = 70;
	private Frame frame = null;
	private Canvas canvas = null;
	private Graphics graph = null;
	private BufferStrategy strategy = null;
	private boolean game_over = false;
	private boolean paused = false;
	private int score = 0;
	private int grow = 0;
	private int seconde, minute, milliseconde = 0; // Clock values
	private long cycleTime = 0;
	private long sleepTime = 0;
	private int bonusTime = 0;
	private int malusTime = 0;
	public boolean inittrue= false;
	private int backgroundright = 300;
	private int backgroundDown = 10;
	JButton loginButton = new JButton("Login");
	JTextField idField = new JTextField();
	JTextField passwordField = new JTextField();
		
	
	

	/**
	 * @param args the command line arguments
	 */
//	public static void main(String[] args) {
//		Server game = new Server();
//		game.init();
//		game.mainLoop();
//	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.mainLoop();
	}
	public ServerUIControl() {
		super();
		frame = new Frame();
		canvas = new Canvas();
		grid = new int[gameSize][gameSize];
		snake = new int[gameSize * gameSize][2];
		this.init();
		this.renderGame();
		this.mainLoop();
	}
	
	private void Login() {
		ServerDB serverdb = new ServerDB();
		serverdb.Updata(serverdb.getMap(), serverdb.getDB());
		if (serverdb.Login("001", "123456", serverdb.getMap()) != null) {
			System.out.println("success login");
		}
	}

	public void init() {
		//connect db
//		ServerDB serverdb = new ServerDB();
//		serverdb.Updata(serverdb.getMap(), serverdb.getDB());
//		//login
//		if (serverdb.Login("001", "123456", serverdb.getMap()) != null) {
//			System.out.println("success login");
//		}
		frame.setSize(width + 310, height + 300);
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 300, height + 300);
		loginButton.setBounds(25,20+backgroundDown+200 , backgroundright-40, 30);
		idField.setBounds(25, 20+backgroundDown+80, backgroundright-40, 30);
		passwordField.setBounds(25, 20+backgroundDown+150, backgroundright-40, 30);
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			Login();
			System.out.println("Mouse Pressed");
			}
			});
//		idField.setBackground(Color.yellow);
		frame.add(idField);
		frame.add(passwordField);
		frame.add(loginButton);
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

	public void mainLoop() {
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
				graph.setColor(Color.black);
//				graph.fillRect(x, y, gridUnit, gridUnit);
				graph.drawRect(backgroundright-1, backgroundDown-1, width+1 ,height+1);

				graph.drawRect(10, backgroundDown-1, backgroundright-15 ,height/3);
				graph.setColor(Color.WHITE);
				graph.fillRect(0+backgroundright, backgroundDown, width, height);
				// Draw snake, bonus ...
				int gridCase = EMPTY;
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						gridCase = grid[i][j];
						switch (gridCase) {
						case SNAKE:
							graph.setColor(Color.BLUE);
							graph.fillOval(i * gridUnit+backgroundright, j * gridUnit+backgroundDown, gridUnit, gridUnit);
							break;
						case FOOD_BONUS:
							graph.setColor(Color.darkGray);
							graph.fillOval(i * gridUnit+backgroundright + gridUnit / 4, j * gridUnit+backgroundDown + gridUnit / 4, gridUnit / 2,
									gridUnit / 2);
							break;
						case FOOD_MALUS:
							graph.setColor(Color.RED);
							graph.fillOval(i * gridUnit+backgroundright + gridUnit / 4, j * gridUnit+backgroundDown + gridUnit / 4, gridUnit / 2,
									gridUnit / 2);
							break;
						case BIG_FOOD_BONUS:
							graph.setColor(Color.GREEN);
							graph.fillOval(i * gridUnit+backgroundright + gridUnit / 4, j * gridUnit+backgroundDown + gridUnit / 4, gridUnit / 2,
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
					graph.drawString("YOUR SCORE : " + score, height / 2 - 40, height / 2 + 50);
					graph.drawString("YOUR TIME : " + getTime(), height / 2 - 42, height / 2 + 100);
				} else if (paused) {
					graph.setColor(Color.RED);
					graph.drawString("PAUSED", height / 2 - 30, height / 2);
				}
				graph.setColor(Color.BLACK);
				graph.drawString("SCORE = " + score, backgroundright+10, 20+backgroundDown);
				graph.drawString("TIME = " + getTime(), backgroundright+190, 20+backgroundDown); // Clock
				graph.drawString("Login", backgroundright/2-20, 20+backgroundDown);
				graph.drawString("ID :", 25, 20+backgroundDown+30);
				graph.drawString("Password :", 25, 20+backgroundDown+100);
				graph.dispose();
			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());
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

	private void placeBonus(int bonus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (grid[x][y] == EMPTY) {
			grid[x][y] = bonus_type;
		} else {
			placeBonus(bonus_type);
		}
	}

	private void placeMalus(int malus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (grid[x][y] == EMPTY) {
			grid[x][y] = malus_type;
		} else {
			placeMalus(malus_type);
		}
	}

	private void gameOver() {
		game_over = true;
	}

	// IMPLEMENTED FUNCTIONS
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

	public void windowClosing(WindowEvent we) {
		System.exit(0);
	}

	// UNNUSED IMPLEMENTED FUNCTIONS
	public void keyTyped(KeyEvent ke) {
	}

	public void keyReleased(KeyEvent ke) {
	}

	public void windowOpened(WindowEvent we) {
	}

	public void windowClosed(WindowEvent we) {
	}

	public void windowIconified(WindowEvent we) {
	}

	public void windowDeiconified(WindowEvent we) {
	}

	public void windowActivated(WindowEvent we) {
	}

	public void windowDeactivated(WindowEvent we) {
	}
}
