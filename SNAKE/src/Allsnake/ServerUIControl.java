
/**
 * 
 */
package Allsnake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.sun.java.util.jar.pack.Instruction.Switch;

/**
* @author Peuch
*/
public class ServerUIControl implements KeyListener, WindowListener {
	// KEYS MAP
//	private static ServerUIControl serverUIControl = new ServerUIControl();
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
	private int[][] grid;
	private Snake snake = null;

	private int height = 1000;
	private int width = 1000;
	private int gameSize = 100;
	public Map map = null;
	private long speed = 70;
	private Frame frame;
	private Canvas canvas;
	private Graphics graph = null;
	private BufferStrategy strategy = null;
	private boolean game_over = false;
	private boolean paused = false;

	private int second, minute, milliseconde=0; // Clock values
	private long cycleTime = 0;
	private long sleepTime = 0;

	private int backgroundright = 300;
	private int backgroundDown = 10;

	//Buttons
	JButton loginButton = new JButton("Login");
	JButton addRobotButton = new JButton("Add Robot");
	JButton setTimeButton = new JButton("Set");

	//TextFields
	JTextField idField = new JTextField();
	JTextField passwordField = new JTextField();
	JTextField TimeField = new JTextField();

	private Player[] playerList = new Player[0];

	private int[] realPlayListOrder = new int[0];

	//The serverDb that store all the player's account
	ServerDB serverdb;
	//Create a pool to handle the concurrent login task
	ExecutorService LoginExecutorService = Executors.newCachedThreadPool();

	public ServerUIControl() {
		frame = new Frame();
		canvas = new Canvas();
		map = new Map();
		grid = Map.getMap().getgrid();
		initUI();
		renderGame();
		mainLoop();
	}

	/**
	 * Initialize basic setting and components in frame
	 */
	public void initUI() {
		minute = 5;
		frame.setSize(width + 340, height+50 );
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 300, height + 300);
//		Add label and text box and login button.
		frame.add(canvas);
		frame.addWindowListener(this);
		frame.dispose();
		frame.validate();
		frame.setTitle("Snake");
		frame.setVisible(true);
		canvas.addKeyListener(this);
		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.WHITE);
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		graph = strategy.getDrawGraphics();
		addButtons();
		addTextFields();
		initGame();
		renderGame();
	}

	/**
	 * Create all the buttons in frame
	 * Add MouseListener for all the created buttons in frame
	 */
	public void addButtons(){

		//Initialize buttons and add them to frame
		loginButton.setBounds(25,20 + backgroundDown + 200 , backgroundright - 40, 30);
		addRobotButton.setBounds(25, 20 + backgroundDown + 940, backgroundright - 40, 30);
		setTimeButton.setBounds(25,20 + backgroundDown + 290 , backgroundright - 40, 30);
		frame.add(loginButton);
		frame.add(setTimeButton);
		frame.add(addRobotButton);

		//setTimeButton Listener
		setTimeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Get the input from TextField
				String time =TimeField.getText();
				SimpleDateFormat format=new SimpleDateFormat("HH:mm");

				//judge it is right format
				boolean dateflag=true;
				try
				{
					java.util.Date date = format.parse(time);
				} catch (Exception e1)
				{
					dateflag=false;
				}
				if(dateflag==false) {
					JOptionPane.showMessageDialog(null, "Error time format.Example(05:00)","", JOptionPane.INFORMATION_MESSAGE);
				}else {
					setMinute(Integer.parseInt( time.split(":")[0] ));
					setSecond(Integer.parseInt( time.split(":")[1] ));
					JOptionPane.showMessageDialog(null, "Success set time","", JOptionPane.INFORMATION_MESSAGE);
				}
			}});

		//addRobot Button listener
		addRobotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addRobot();
			}
		});

		//login button listener
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//player login to game
				playerLogin(idField.getText(),passwordField.getText());
				System.out.println("Player : "+idField.getText()+"login successful");
			}
		});
	}

	/**
	 * Create all the TextFileds in frame
	 */
	public void addTextFields(){

		//Initialize all the TextFields
		idField.setBounds(25, 20+backgroundDown+80, backgroundright-40, 30);
		passwordField.setBounds(25, 20+backgroundDown+150, backgroundright-40, 30);
		TimeField.setBounds(100, backgroundDown+260, backgroundright-170, 30);
		idField.setText("001");//Default player id
		passwordField.setText("123456");
		TimeField.setText("05:00");

		//Add them to the frame
		frame.add(idField);
		frame.add(passwordField);
		frame.add(TimeField);
	}

	/**
	 * Verify whether id and password located a account in DB.
	 * @param id
	 * @param password
	 */
	public synchronized void playerLogin(String id, String password) {
		serverdb = new ServerDB(id,password);
		serverdb.Update(serverdb.getMap(),serverdb.getDB());
		java.util.concurrent.Future<String> future = LoginExecutorService.submit(serverdb);
		try {
			String result=future.get();
			System.out.println(result);
		if (result!= "") {
			System.out.println("success login");
			//add player and snake
			this.addPlayer(new Player("001", gameSize));
			addRealPlaylistOrder(playerList.length-1);
			snake = playerList[playerList.length-1].getSnake();
			RandomBirth(snake);	
			setRealPlayerKeypress();
			//Draw a massage box to show login successful
			JOptionPane.showMessageDialog(null, "Success Login","", JOptionPane.INFORMATION_MESSAGE);
		}else {
			//Draw a massage box to show login failed
			JOptionPane.showMessageDialog(null, "Fail Login","", JOptionPane.INFORMATION_MESSAGE);
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Add a robot to the game
	 */
	public synchronized void addRobot() {
		System.out.println("success login");
		//add Robot player
		for(int i=0;i<10;i++) {
			addPlayer(new Player("Robot", gameSize));
			playerList[playerList.length-1].setIsRobot(true);
			snake = playerList[playerList.length-1].getSnake();
			RandomBirth(snake);
		}
	}


	/**
	 * If the player start playing or his snake get killed then give the snake a new place to reborn
	 * @param snake
	 */
	public synchronized void RandomBirth(Snake snake) {
	
		//Init snake
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
			RandomBirth(playerList[playerList.length-1].getSnake());
		}
	}


	/**
	 * Games main loop which will update the map and snake
	 */
	public void mainLoop() {
		while (!game_over) {
			cycleTime = System.currentTimeMillis();
			if (!paused) {
				if(second ==0 &&minute == 0) {
		            game_over = true;
	        }
					int nu = playerList.length/20;
					int remain = playerList.length%20;
					for(int i=0 ;i<20;i++) {
					LoginExecutorService.execute(new Dateprocess(playerList,i*nu,(i+1)*nu));
					}
					LoginExecutorService.execute(new Dateprocess(playerList,20*nu,20*nu+remain));
				for(int i = 0; i< playerList.length; i++) {
					if(playerList[i].getSnake().getGameover() == true) {
						playerList[i].InitSnake();
						RandomBirth(playerList[i].getSnake());
					}
				}
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
		for (int i =0 ;i<new Random().nextInt(4)+2;i++) {
		placeBonus(FOOD_BONUS);
		}
	}

	private  void renderGame() {
		int gridUnit = height / gameSize;
		canvas.paint(graph);int cout=0;
		do {
			do {
				
				graph = strategy.getDrawGraphics();
				// Draw Background
				graph.setColor(Color.black);
//				Draw a border line
				graph.drawRect(backgroundright-1, backgroundDown-1, width+1 ,height+1);
				graph.drawRect(10, backgroundDown-1+ height/3+7, backgroundright-15 ,height-350);

				graph.drawRect(10, backgroundDown-1, backgroundright-15 ,height/3);
				graph.setColor(Color.WHITE);
				graph.fillRect(0+backgroundright, backgroundDown, width, height);
				// Draw snake, bonus ...
				int gridCase = EMPTY;
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						if(grid[i][j]==SNAKE) {
							grid[i][j]=EMPTY;
						}
				}
			}
				if(playerList !=null) {
				for(int i = 0; i< playerList.length; i++) {
					for (int z = 0; z< gameSize * gameSize; z++) {
						if ((playerList[i].getSnake().getSnakeInfo(z, 0) < 0) || (playerList[i].getSnake().getSnakeInfo(z, 1) < 0)) {
							break;
						}
						grid[playerList[i].getSnake().getSnakeInfo(z, 0)][playerList[i].getSnake().getSnakeInfo(z, 1)] = SNAKE;
					}
				}
				}
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						gridCase = grid[i][j];
						switch (gridCase) {
						case SNAKE:
							graph.setColor(Color.BLUE);
							graph.fillOval(i * gridUnit+backgroundright, j * gridUnit+backgroundDown, gridUnit, gridUnit);
							graph.setColor(Color.PINK);
							if(realPlayListOrder!=null) {
								if(realPlayListOrder.length>0) {
									for (int z = 0; z< gameSize * gameSize; z++) {
										if ((playerList[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 0) < 0) ||
												(playerList[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 1) < 0)) {
											break;
										}
										graph.fillOval(playerList[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 0)*
												gridUnit+backgroundright, playerList[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 1)
												*gridUnit+backgroundDown,gridUnit,gridUnit);
									}
								}
								graph.setColor(Color.ORANGE);
								if (realPlayListOrder.length>1) {
									for (int z = 0; z< gameSize * gameSize; z++) {
										if ((playerList[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 0) < 0) ||
												(playerList[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 1) < 0)) {
											break;
										}
										graph.fillOval(playerList[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 0)*
												gridUnit+backgroundright, playerList[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 1)*
												gridUnit+backgroundDown,gridUnit,gridUnit);
									}
								}
								graph.setColor(Color.DARK_GRAY);
								if (realPlayListOrder.length>2) {
									for (int z = 0; z< gameSize * gameSize; z++) {
										if ((playerList[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 0) < 0) ||
												(playerList[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 1) < 0)) {
											break;
										}
										graph.fillOval(playerList[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 0)*
												gridUnit+backgroundright, playerList[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 1)*
												gridUnit+backgroundDown,gridUnit,gridUnit);
									}
								}
								graph.setColor(Color.LIGHT_GRAY);
								if (realPlayListOrder.length>3) {
									for (int z = 0; z< gameSize * gameSize; z++) {
										if ((playerList[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 0) < 0) ||
												(playerList[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 1) < 0)) {
											break;
										}
										graph.fillOval(playerList[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 0)*
												gridUnit+backgroundright, playerList[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 1)*
												gridUnit+backgroundDown,gridUnit,gridUnit);
									}
								}
							}
							break;
						case FOOD_BONUS:
							graph.setColor(Color.darkGray);
							graph.fillOval(i * gridUnit+backgroundright + gridUnit / 4, j *
											gridUnit+backgroundDown + gridUnit / 4, gridUnit ,
									gridUnit );
							break;
						case FOOD_MALUS:
							graph.setColor(Color.RED);
						case BIG_FOOD_BONUS:
							graph.setColor(Color.GREEN);
							graph.fillOval(i * gridUnit+backgroundright + gridUnit / 4, j *
											gridUnit+backgroundDown + gridUnit / 4, gridUnit *2,
									gridUnit *2);
							break;
						default:
							break;
						}
					}
				}
				graph.setFont(new Font(Font.SANS_SERIF, Font.BOLD, height / 40));

				graph.setColor(Color.BLACK);
				graph.drawString("TIME = " + getTime(), backgroundright + 10, 20 + backgroundDown); // Clock
				graph.drawString("Login", backgroundright/2 - 20, 20 + backgroundDown);
				graph.drawString("ID :", 25, 20 + backgroundDown + 30);
				graph.drawString("Password :", 25, 20 + backgroundDown + 100);
				graph.drawString("Time", 25, backgroundDown + 250);
				graph.drawString("Scoreboard", backgroundright/2 - 60, 20 + backgroundDown + height/3 + 7);
				int a=0;

				if (game_over) {
					graph.setColor(Color.RED);
					graph.drawString("GAME OVER", height / 2 - 30, height / 2);
				}

				for(int i = 0; i< playerList.length; i++) {
					if(playerList[i].getIsRobot()==false) {
					graph.drawString(playerList[i].getID(), 25, 20+backgroundDown+ height/3+7+(a+1)*30);
					String scoreString = ""+ playerList[i].getScore();
					graph.setColor(Color.white);
					graph.fillRect(200, 20+backgroundDown+ height/3+15+(a)*30,30,30);
					graph.setColor(Color.BLACK);
					graph.drawString("" + playerList[i].getScore(), 200, 20+backgroundDown+ height/3+7+(a+1)*30);
					a++;
					}
				}
				graph.dispose();
			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());

	}

	private String getTime() {
		String temps = new String(minute + ":" + second);
		if(playerList.length >0) {
		if ( paused)
			return temps;
		milliseconde--;
		if (second == -1) {
			second = 59;
			minute--;
		}
		if (milliseconde == -14) {
			second--;
			milliseconde = 0;
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

	public void addPlayer(Player a) {
		
		Player[] newPlayerListPlayers = new Player[playerList.length+1];
		for(int i = 0; i< playerList.length; i++) {
			newPlayerListPlayers[i] = playerList[i];
			
		}
		newPlayerListPlayers[playerList.length] = a;
		playerList = newPlayerListPlayers;
	}

	// IMPLEMENTED FUNCTIONS
	public synchronized void  keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		if(snake!=null) {
			//judge the input keypress and add direction to the buffer
			for(int i =0;i<realPlayListOrder.length;i++) {
				playerList[realPlayListOrder[i]].judgeInput(code);
			}

		switch (code) {		
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_SPACE:
				paused = !paused;
			break;
		default:
			break;
		}

		}
	}

	//add new RealPlayerOrder to the Order list

	/**
	 *
	 * @param a
	 */
	public void addRealPlaylistOrder(int a) {
		int[] newOrderList = new int[realPlayListOrder.length+1];
		System.arraycopy(realPlayListOrder, 0, newOrderList, 0, realPlayListOrder.length);
		newOrderList[newOrderList.length-1] = a;
		realPlayListOrder = newOrderList;
		System.out.println(realPlayListOrder.length);
	}

	/**
	 *
	 */
	public void setRealPlayerKeypress() {
		switch(realPlayListOrder.length){
			case 1:
				playerList[realPlayListOrder[0]].setKeypress(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
				break;
			case 2:
				playerList[realPlayListOrder[1]].setKeypress(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A);
				break;
			case 3:
				playerList[realPlayListOrder[2]].setKeypress(KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_J);
				break;
			case 4:
				playerList[realPlayListOrder[3]].setKeypress(KeyEvent.VK_G, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_V);
				break;
			default:
				break;
		}
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void windowClosing(WindowEvent we) {
		System.exit(0);
	}

	// Necessary IMPLEMENTED FUNCTIONS
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

