
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.sun.java.util.jar.pack.Instruction.Switch;

/**
* @author Peuch
*/
public class ServerUIControl implements KeyListener, WindowListener {

	// GRID CONTENT
	public final static int EMPTY = 0;
	public final static int FOOD_BONUS = 1;
	public final static int FOOD_MALUS = 2;
	public final static int BIG_FOOD_BONUS = 3;
	public final static int SNAKE = 4;
	private int[][] gridMap;
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
	private JButton loginButton = new JButton("Login");
	private JButton addRobotButton = new JButton("Add Robot");
	private JButton setTimeButton = new JButton("Set");

	//TextFields
	private JTextField idField = new JTextField();
	private JTextField passwordField = new JTextField();
	private JTextField TimeField = new JTextField();

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
		gridMap = Map.getMap().getGrid();
		initUI();
		mainLoop();
	}

	/**
	 * For the Convenience of test
	 * @param test
	 */
	public ServerUIControl(String test){
		frame = new Frame();
		canvas = new Canvas();
		map = new Map();
		gridMap = Map.getMap().getGrid();
//		initUI();
	}

	/**
	 * Initialize basic setting and components in frame
	 */
	public void initUI() {
		addButtons();
		addTextFields();
		initGame();
		minute = 5;
		frame.setSize(width + 340, height + 50 );
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 340, height + 50);
		frame.add(canvas);
		frame.addWindowListener(this);
		frame.dispose();
		frame.validate();
		frame.setTitle("Snake");
		frame.setVisible(true);
		canvas.addKeyListener(this);
		canvas.setIgnoreRepaint(true);
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		graph = strategy.getDrawGraphics();
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

		setTimeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(2);
				//Get the input from TextField
				String time =TimeField.getText();
				SimpleDateFormat format=new SimpleDateFormat("HH:mm");

				//judge if it is right format
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
			}
		});
		//setTimeButton Listener


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
				try {
					playerLogin(idField.getText(),passwordField.getText());
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
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
	 * Verify whether id and password located a account in DB and create the snake.
	 * @param id
	 * @param password
	 */
	public synchronized void playerLogin(String id, String password) throws InterruptedException {
		try {
			//init server db
			serverdb = new ServerDB(id,password);
			//Add all the players account to table
			java.util.concurrent.Future<String> future = LoginExecutorService.submit(serverdb);
			String result=future.get();
			System.out.println(result);
			if (result!= "") {
				System.out.println("success login");
				//add player and snake
				this.addPlayer(new Player(result, gameSize));
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
		}catch (InterruptedException | ExecutionException e) {
			System.out.println(e);
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
		if(gridMap[random1][random2] != SNAKE) {
		
		snake.setSnakeInfo(0, 0, random1);
		
        snake.setSnakeInfo(0, 1, random2);
		gridMap[random1][random2] = SNAKE;
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
				updateSnake();
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

	/**
	 * Update snake's position on the map
	 */
	public void updateSnake(){
		int nu = playerList.length/20;
		int remain = playerList.length%20;
		for(int i=0 ;i<20;i++) {
			LoginExecutorService.execute(new DateProcess(playerList,i*nu,(i+1)*nu));
		}
		LoginExecutorService.execute(new DateProcess(playerList,20*nu,20*nu+remain));
		for(int i = 0; i< playerList.length; i++) {
			if(playerList[i].getSnake().getGameover()) {
				playerList[i].InitSnake();
				RandomBirth(playerList[i].getSnake());
			}
		}
	}


	/**
	 * Clear the grid and place some bonus
	 */
	private void initGame() {
		// Initialise tabs
		for (int i = 0; i < gameSize; i++) {
			for (int j = 0; j < gameSize; j++) {
				gridMap[i][j] = EMPTY;
			}
		}
		for (int i =0 ;i<new Random().nextInt(4)+2;i++) {
		placeBonus(FOOD_BONUS);
		}
	}

	/**
	 * Set the background interface of Snake
	 */
	private  void renderGame() {
		canvas.paint(graph);
		do {
			do {
				graph = strategy.getDrawGraphics();
				// Draw Background
				graph.setColor(Color.RED);
				// Draw rectangles (game,score board,login) interface
				graph.drawRect(backgroundright - 1, backgroundDown - 1, width + 1 ,height + 1);// Game rectangles
				graph.drawRect(10, backgroundDown - 1+ height/3+7, backgroundright - 15 ,height - 350);// Draw score board rectangles
				graph.drawRect(10, backgroundDown - 1, backgroundright - 15 ,height/3);//Draw login rectangles
				graph.setColor(Color.WHITE);
				graph.fillRect(backgroundright, backgroundDown, width, height);
				// Draw snake, bonus ...
				int gridCase = EMPTY;
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						if(gridMap[i][j] == SNAKE) {
							gridMap[i][j] = EMPTY;
						}
				}
			}
				if(playerList !=null) {
				for(int i = 0; i< playerList.length; i++) {
					for (int z = 0; z< gameSize * gameSize; z++) {
						if ((playerList[i].getSnake().getSnakeInfo(z, 0) < 0) || (playerList[i].getSnake().getSnakeInfo(z, 1) < 0)) {
							break;
						}
						gridMap[playerList[i].getSnake().getSnakeInfo(z, 0)][playerList[i].getSnake().getSnakeInfo(z, 1)] = SNAKE;
					}
				}
				}
				//
				int gridUnit = height / gameSize;

				//Retrieval of all nodes in grid
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						gridCase = gridMap[i][j];
						switch (gridCase) {
						case SNAKE:
							graph.setColor(Color.BLUE);
							graph.fillOval(i * gridUnit + backgroundright, j * gridUnit+backgroundDown, gridUnit, gridUnit);
							//The first player
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
								//The second player
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
								//The third player
								graph.setColor(Color.CYAN);
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
								//The fourth player
								graph.setColor(Color.MAGENTA);
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
							graph.setColor(Color.ORANGE);
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
				//Set background graph style
				graph.setFont(new Font(Font.SANS_SERIF, Font.BOLD, height / 40));
				graph.setColor(Color.BLACK);
				graph.drawString("TIME = " + getTime(), backgroundright + 10, 20 + backgroundDown); // Clock
				graph.drawString("Login", backgroundright/2 - 20, 20 + backgroundDown);
				graph.drawString("ID :", 25, 20 + backgroundDown + 30);
				graph.drawString("Password :", 25, 20 + backgroundDown + 100);
				graph.drawString("Time", 25, backgroundDown + 250);
				graph.drawString("Scoreboard", backgroundright/2 - 60, 20 + backgroundDown + height/3 + 7);
				int a=0;
				//Game over style
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

	/**
	 * Caculate player play time
	 * @return temps
	 */
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

	/**
	 * Place a type of bonus in map
	 * @param bonus_type
	 */
	public void placeBonus(int bonus_type) {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (gridMap[x][y] == EMPTY) {
			gridMap[x][y] = bonus_type;
		} else {
			placeBonus(bonus_type);
		}
	}

	/**
	 * Add a new player into the playList
	 * @param a
	 */
	public void addPlayer(Player a) {
		
		Player[] newPlayerListPlayers = new Player[playerList.length+1];
		for(int i = 0; i< playerList.length; i++) {
			newPlayerListPlayers[i] = playerList[i];
			
		}
		newPlayerListPlayers[playerList.length] = a;
		playerList = newPlayerListPlayers;
	}

	/**
	 * Pause the game by press 'space' on keyboard or exit the game by press 'esc'
	 * @param ke
	 */
	public synchronized void  keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();
		System.out.println("Keypress "+code);

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

	/**
	 * Add a player in realPlayerList
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
	 *Give 4 players original key for their snake's movement
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

	public Player[] getPlayerList() {
		return playerList;
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

	public void setGridMap(int[][] gridMap){
		this.gridMap = gridMap;
	}
	public JButton getLoginButton() {
		return loginButton;
	}

	public JButton getAddRobotButton() {
		return addRobotButton;
	}

	public JButton getSetTimeButton() {
		return setTimeButton;
	}

	public JTextField getIdField() {
		return idField;
	}

	public JTextField getPasswordField() {
		return passwordField;
	}

	public JTextField getTimeField() {
		return TimeField;
	}

	public boolean isGame_over() {
		return game_over;
	}

	public boolean isPaused() {
		return paused;
	}

	public Map getMap(){return map;}

	//IMPLEMENTED FUNCTIONS
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

