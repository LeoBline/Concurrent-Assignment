/**
 * 
 */
package Allsnake;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.sun.prism.Image;

/**
* @author Peuch
*/
public class ServerUIControl implements KeyListener, WindowListener {
	// KEYS MAP
	private static ServerUIControl serverUIControl = new ServerUIControl();
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
	private Snake snake = null;
	private int direction = -1;
	private int next_direction = -1;
	private int height = 720;
	private int width = 720;
	private int gameSize = 80;
	public Map map = null;
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
	public int bonusTime = 0;
	public int malusTime = 0;
	public boolean inittrue= false;
	private int backgroundright = 300;
	private int backgroundDown = 10;
	JButton loginButton = new JButton("Login");
	JTextField idField = new JTextField();
	JTextField passwordField = new JTextField();
	ServerDB serverdb;
	private Player[] playerlist = new Player[0];
	
	
	

	/**
	 * @param args the command line arguments
	 */

	public ServerUIControl() {

		frame = new Frame();
		canvas = new Canvas();
		
		map = new Map();
		grid = Map.getMap().getgrid();
		this.init();
		
		serverdb = new ServerDB();
		serverdb.Updata(serverdb.getMap(), serverdb.getDB());
		
		
		this.renderGame();
		this.mainLoop();
	}
	//fix the bug
	public synchronized  void Login(String id,String password) {
//Verify the password of the filled database account.
		if (serverdb.Login(id, password, serverdb.getMap()) != "") {
			System.out.println("success login");
			//add player and snake
			this.addplayer(new Player("001", gameSize));
			snake = playerlist[playerlist.length-1].getSnake();
			RandomBirth(snake);
			JOptionPane.showMessageDialog(null, "Success Login","", JOptionPane.INFORMATION_MESSAGE);

		}else {
			JOptionPane.showMessageDialog(null, "Fail Login","", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	//Random place to generate snakes for new players
	public void RandomBirth(Snake snake) {
	
		//init snake
		for (int i = 0; i < gameSize * gameSize; i++) {
			snake.setSnakeInfo(i, 0, -1);
			snake.setSnakeInfo(i, 1, -1);

		}
		int random1 = (int) (Math.random()*gameSize);  
		int random2= (int) (Math.random()*gameSize);  
		//Determine if there is a snake at a randomly generated location
		if(grid[random1][random2] != SNAKE) {
		
		snake.setSnakeInfo(0, 0, random1);
		
        snake.setSnakeInfo(0, 1, random2);
		grid[random1][random2] = SNAKE;
		}else {
			RandomBirth(playerlist[playerlist.length-1].getSnake());
		}
	}

	public void init() {
//
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
			Login(idField.getText(),passwordField.getText());
			System.out.println(idField.getText());
			}
			});
//		Add label and text box and login button.
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
			Thread aThread;
			cycleTime = System.currentTimeMillis();
			if (!paused) {
				 aThread = new Thread(new Dateprocess(playerlist));
				aThread.start();
				for(int i =0 ; i< playerlist.length;i++) {
					if(playerlist[i].getSnake().getGameover() == true) {
						playerlist[i].InitSnake();
						RandomBirth(playerlist[i].getSnake());
					}
				}
//				moveSnake(); 
			}

			renderGame();
			cycleTime = System.currentTimeMillis() - cycleTime;
			sleepTime = speed - cycleTime;
			if (sleepTime < 0)
				sleepTime = 0;
			try {
				Thread.sleep(sleepTime);
				
			} catch (InterruptedException ex) {
				Logger.getLogger(ServerUIControl.class.getName()).log(Level.SEVERE, null, ex);
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
//				Draw a border line
				graph.drawRect(backgroundright-1, backgroundDown-1, width+1 ,height+1);
				graph.drawRect(10, backgroundDown-1+ height/3+7, backgroundright-15 ,height+1);

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
					graph.drawString("PAUSED", backgroundright+500, 20+backgroundDown);

				}
				graph.setColor(Color.BLACK);
				graph.drawString("SCORE = " + score, backgroundright+10, 20+backgroundDown);
				graph.drawString("TIME = " + getTime(), backgroundright+190, 20+backgroundDown); // Clock
//				System.out.println(getTime());
				graph.drawString("Login", backgroundright/2-20, 20+backgroundDown);
				graph.drawString("ID :", 25, 20+backgroundDown+30);
				graph.drawString("Password :", 25, 20+backgroundDown+100);
				graph.drawString("Scoreboard", backgroundright/2-60, 20+backgroundDown+ height/3+7);
//				graph.drawString("SCORE = " + score, 150, 20+backgroundDown+ height/3+7+30);
				for(int i=0 ;i<playerlist.length;i++) {
					graph.drawString(playerlist[i].getID(), 25, 20+backgroundDown+ height/3+7+(i+1)*30);
					String scoreString = ""+playerlist[i].getScore();
					graph.setColor(Color.white);
					graph.fillRect(200, 20+backgroundDown+ height/3+15+(i)*30,30,30);
					graph.setColor(Color.BLACK);
					graph.drawString("" + playerlist[i].getScore(), 200, 20+backgroundDown+ height/3+7+(i+1)*30);
				}
				graph.dispose();
			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());
	}

	private String getTime() {
		String temps = new String(minute + ":" + seconde);
		if(playerlist.length >0) {
		if (playerlist[0].getSnake().getDirection() == -1 || paused)
			return temps;
		milliseconde++;
		System.out.println(milliseconde);
		if (milliseconde == 14) {
			seconde++;
			milliseconde = 0;
		}
		if (seconde == 60) {
			seconde = 0;
			minute++;
		}
		return temps;
		}else {
			return temps;
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

	private void gameOver() {
		game_over = true;
	}
	public void addplayer(Player a) {
		
		Player[] newplayerlistPlayers = new Player[playerlist.length+1];
		for(int i =0 ; i<playerlist.length;i++) {
			newplayerlistPlayers[i] = playerlist[i];
			
		}
		newplayerlistPlayers[playerlist.length] = a;
		playerlist = newplayerlistPlayers;
	}

	// IMPLEMENTED FUNCTIONS
	public synchronized void  keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		Dimension dim;
		if(snake!=null) {
		switch (code) {
		//player1
		case KeyEvent.VK_UP:
			if (playerlist[0].getSnake().getDirection() != DOWN) {
//				next_direction = UP;
				playerlist[0].Getbuffer().append(UP);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (playerlist[0].getSnake().getDirection() != UP) {
//				next_direction = DOWN;
				playerlist[0].Getbuffer().append(DOWN);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (playerlist[0].getSnake().getDirection()  != RIGHT) {
//				next_direction = LEFT;
				playerlist[0].Getbuffer().append(LEFT);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (playerlist[0].getSnake().getDirection()  != LEFT) {
//				next_direction = RIGHT;
				playerlist[0].Getbuffer().append(RIGHT);
			}
			break;
		
			//player2
		case KeyEvent.VK_W:
			if (playerlist[1].getSnake().getDirection()  != DOWN &&playerlist.length==2) {
//				next_direction = RIGHT;
				playerlist[1].Getbuffer().append(UP);
			}
			break;
			
		case KeyEvent.VK_S:
			if (playerlist[1].getSnake().getDirection() != UP &&playerlist.length==2) {
//				next_direction = DOWN;
				playerlist[1].Getbuffer().append(DOWN);
			}
			break;
		case KeyEvent.VK_A:
			if (playerlist[1].getSnake().getDirection()  != RIGHT &&playerlist.length==2) {
//				next_direction = LEFT;
				playerlist[1].Getbuffer().append(LEFT);
			}
			break;
		case KeyEvent.VK_D:
			if (playerlist[1].getSnake().getDirection()  != LEFT &&playerlist.length==2) {
//				next_direction = RIGHT;
				playerlist[1].Getbuffer().append(RIGHT);
			}
			break;
			
		case KeyEvent.VK_I:
			if (playerlist[2].getSnake().getDirection()  != DOWN &&playerlist.length==3) {
//				next_direction = RIGHT;
				playerlist[2].Getbuffer().append(UP);
			}
			break;
			
		case KeyEvent.VK_K:
			if (playerlist[2].getSnake().getDirection() != UP &&playerlist.length==3) {
//				next_direction = DOWN;
				playerlist[2].Getbuffer().append(DOWN);
			}
			break;
		case KeyEvent.VK_J:
			if (playerlist[2].getSnake().getDirection()  != RIGHT &&playerlist.length==3) {
//				next_direction = LEFT;
				playerlist[2].Getbuffer().append(LEFT);
			}
			break;
		case KeyEvent.VK_L:
			if (playerlist[2].getSnake().getDirection()  != LEFT &&playerlist.length==3) {
//				next_direction = RIGHT;
				playerlist[2].Getbuffer().append(RIGHT);
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
	}
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

	/**
	 * @return
	 */
	public static  ServerUIControl getSever() {
		// TODO Auto-generated method stub

		return serverUIControl;
	}

	/**
	 * @return
	 */
	public int getBonusTime() {
		// TODO Auto-generated method stub
		return bonusTime;
	}

	/**
	 * @return
	 */
	public int getmalusTime() {
		// TODO Auto-generated method stub
		return malusTime;
	}

	/**
	 * @param i
	 */
	public void setmalusTime(int i) {
		// TODO Auto-generated method stub
		malusTime = i;
		
	}
}
