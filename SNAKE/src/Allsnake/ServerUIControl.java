
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
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.sun.corba.se.impl.orbutil.closure.Future;
//import com.sun.java.util.jar.pack.Instruction.Switch;
import com.sun.prism.Image;

import sun.security.util.Length;
import sun.util.calendar.BaseCalendar.Date;

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
	private int[][] grid = null;
	private Snake snake = null;
	private int direction = -1;
	private int next_direction = -1;
	private int height = 1000;
	private int width = 1000;
	private int gameSize = 100;
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
	private int seconde, minute, milliseconde=0; // Clock values
	private long cycleTime = 0;
	private long sleepTime = 0;
	public int bonusTime = 0;
	public int malusTime = 0;
	public boolean inittrue= false;
	private int backgroundright = 300;
	private int backgroundDown = 10;
	JButton loginButton = new JButton("Login");
	JButton AddRobotButton = new JButton("Add Robot");
	JTextField idField = new JTextField();
	JTextField passwordField = new JTextField();
	JTextField TimeField = new JTextField();
	JButton SetTimeButton = new JButton("Set");
	ServerDB serverdb;
	private Player[] playerlist = new Player[0];
	ExecutorService pool= null;
	private int Robotnumber=1;

	
	private int firstplayerOrder=99999;
	private int secondplayerOrder=99999;
	private int thirdplayerOrder=99999;
	private int fourthplayerOrder=99999;
	private int[] realPlayListOrder = new int[0];
	
	
	
	ExecutorService executorService2 = Executors.newCachedThreadPool();
	
	

	/**
	 * @param args the command line arguments
	 */

	public ServerUIControl() {

		frame = new Frame();
		canvas = new Canvas();
		
		map = new Map();
		grid = Map.getMap().getgrid();
		this.init();
		
		
		this.renderGame();
		this.mainLoop();
	}
	//fix the bug
	public synchronized  void Login(String id,String password) {
//Verify the password of the filled database account.
		serverdb = new ServerDB(id,password);
		java.util.concurrent.Future<String> future = executorService2.submit(serverdb);
		try {
			String result=future.get();
		if (result!= "") {
			System.out.println("success login");
			//add player and snake
			this.addplayer(new Player("001", gameSize));
			addRealPlaylistOrder(playerlist.length-1);
			System.out.println("12312312");
			snake = playerlist[playerlist.length-1].getSnake();
			RandomBirth(snake);	
			setRealPlayerkeypress();
			JOptionPane.showMessageDialog(null, "Success Login","", JOptionPane.INFORMATION_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(null, "Fail Login","", JOptionPane.INFORMATION_MESSAGE);
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//add Robot 
	public synchronized  void addRobot() {
			
					System.out.println("success login");
					//add Robot player and snake
					for(int i=0;i<10;i++) {
					this.addplayer(new Player("Robot", gameSize));
					playerlist[playerlist.length-1].setIsRobot(true);
					snake = playerlist[playerlist.length-1].getSnake();
					
					RandomBirth(snake);
					}
			}
	
	
	
	//Random place to generate snakes for new players
	public synchronized void RandomBirth(Snake snake) {
	
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
		minute = 5;
		frame.setSize(width + 340, height+50 );
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 300, height + 300);
		loginButton.setBounds(25,20+backgroundDown+200 , backgroundright-40, 30);
		AddRobotButton.setBounds(25, 20+backgroundDown+940, backgroundright-40, 30);
		idField.setBounds(25, 20+backgroundDown+80, backgroundright-40, 30);
		passwordField.setBounds(25, 20+backgroundDown+150, backgroundright-40, 30);
		TimeField.setBounds(100, backgroundDown+260, backgroundright-170, 30);
		SetTimeButton.setBounds(25,20+backgroundDown+290 , backgroundright-40, 30);
		idField.setText("001");
		passwordField.setText("123456");
		TimeField.setText("05:00");
		SetTimeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

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
					setSeconde(Integer.parseInt( time.split(":")[1] ));
					JOptionPane.showMessageDialog(null, "Success set time","", JOptionPane.INFORMATION_MESSAGE);

					
				}
			}});
		AddRobotButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub


				addRobot();

			}
			});
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
		frame.add(TimeField);
		frame.add(loginButton);
		frame.add(SetTimeButton);
		frame.add(AddRobotButton);
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
				if(seconde ==0 &&minute == 0) {
		            game_over = true;
	        }
					int nu = playerlist.length/20;
					int remain =playerlist.length%20;
					for(int i=0 ;i<20;i++) {
					executorService2.execute(new Dateprocess(playerlist,i*nu,(i+1)*nu));		
					}
					executorService2.execute(new Dateprocess(playerlist,20*nu,20*nu+remain));
				for(int i =0 ; i< playerlist.length;i++) {
					if(playerlist[i].getSnake().getGameover() == true) {
						playerlist[i].InitSnake();
						RandomBirth(playerlist[i].getSnake());
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
	
	public synchronized void resurrectionSnake() {
		for(int i =0 ; i< playerlist.length;i++) {
			if(playerlist[i].getSnake().getGameover() == true) {
				playerlist[i].InitSnake();
				RandomBirth(playerlist[i].getSnake());
			}
		}
	}
	
	public int TruePlayerNumber() {
		int TruePlayernumber =0;
		if(playerlist != null) {
		for(int a =0 ; a <playerlist.length;a++) {
			if(playerlist[a].getIsRobot() == false) {
				TruePlayernumber++;
			}
		}
		}
		return TruePlayernumber;
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
				if(playerlist!=null) {
				for(int i =0;i<playerlist.length;i++) {
					for (int z = 0; z< gameSize * gameSize; z++) {
						if ((playerlist[i].getSnake().getSnakeInfo(z, 0) < 0) || (playerlist[i].getSnake().getSnakeInfo(z, 1) < 0)) {
							break;
						}
						grid[playerlist[i].getSnake().getSnakeInfo(z, 0)][playerlist[i].getSnake().getSnakeInfo(z, 1)] = SNAKE;
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
											if ((playerlist[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 0) < 0) || (playerlist[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 1) < 0)) {
												break;
											}
											graph.fillOval(playerlist[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 0)*gridUnit+backgroundright,playerlist[realPlayListOrder[0]].getSnake().getSnakeInfo(z, 1)*gridUnit+backgroundDown,gridUnit,gridUnit);
										}
									
								}
								graph.setColor(Color.ORANGE);
								if (realPlayListOrder.length>1) {
									for (int z = 0; z< gameSize * gameSize; z++) {
										if ((playerlist[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 0) < 0) || (playerlist[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 1) < 0)) {
											break;
										}
										graph.fillOval(playerlist[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 0)*gridUnit+backgroundright,playerlist[realPlayListOrder[1]].getSnake().getSnakeInfo(z, 1)*gridUnit+backgroundDown,gridUnit,gridUnit);
									}
								}
								graph.setColor(Color.DARK_GRAY);
								if (realPlayListOrder.length>2) {
									for (int z = 0; z< gameSize * gameSize; z++) {
										if ((playerlist[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 0) < 0) || (playerlist[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 1) < 0)) {
											break;
										}
										graph.fillOval(playerlist[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 0)*gridUnit+backgroundright,playerlist[realPlayListOrder[2]].getSnake().getSnakeInfo(z, 1)*gridUnit+backgroundDown,gridUnit,gridUnit);
									}
								}
								graph.setColor(Color.LIGHT_GRAY);
								if (realPlayListOrder.length>3) {
									for (int z = 0; z< gameSize * gameSize; z++) {
										if ((playerlist[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 0) < 0) || (playerlist[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 1) < 0)) {
											break;
										}
										graph.fillOval(playerlist[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 0)*gridUnit+backgroundright,playerlist[realPlayListOrder[3]].getSnake().getSnakeInfo(z, 1)*gridUnit+backgroundDown,gridUnit,gridUnit);
									}
								}
							}
							break;
						case FOOD_BONUS:
							graph.setColor(Color.darkGray);
							graph.fillOval(i * gridUnit+backgroundright + gridUnit / 4, j * gridUnit+backgroundDown + gridUnit / 4, gridUnit ,
									gridUnit );
							break;
						case FOOD_MALUS:
							graph.setColor(Color.RED);
						case BIG_FOOD_BONUS:
							graph.setColor(Color.GREEN);
							graph.fillOval(i * gridUnit+backgroundright + gridUnit / 4, j * gridUnit+backgroundDown + gridUnit / 4, gridUnit *2,
									gridUnit *2);
							break;
						default:
							break;
						}
					}
				}
				graph.setFont(new Font(Font.SANS_SERIF, Font.BOLD, height / 40));

				graph.setColor(Color.BLACK);
				graph.drawString("TIME = " + getTime(), backgroundright+10, 20+backgroundDown); // Clock
//				System.out.println(getTime());
				graph.drawString("Login", backgroundright/2-20, 20+backgroundDown);
				graph.drawString("ID :", 25, 20+backgroundDown+30);
				graph.drawString("Password :", 25, 20+backgroundDown+100);
				graph.drawString("Time", 25, backgroundDown+250);
				graph.drawString("Scoreboard", backgroundright/2-60, 20+backgroundDown+ height/3+7);
//				graph.drawString("SCORE = " + score, 150, 20+backgroundDown+ height/3+7+30);
				int a=0;		
				if (game_over) {
					graph.setColor(Color.RED);
					graph.drawString("GAME OVER", height / 2 - 30, height / 2);
				} else if (paused) {
					graph.setColor(Color.RED);
					graph.drawString("PAUSED", backgroundright+500, 20+backgroundDown);

				}
				for(int i=0 ;i<playerlist.length;i++) {
					if(playerlist[i].getIsRobot()==false) {
					graph.drawString(playerlist[i].getID(), 25, 20+backgroundDown+ height/3+7+(a+1)*30);
					String scoreString = ""+playerlist[i].getScore();
					graph.setColor(Color.white);
					graph.fillRect(200, 20+backgroundDown+ height/3+15+(a)*30,30,30);
					graph.setColor(Color.BLACK);
					graph.drawString("" + playerlist[i].getScore(), 200, 20+backgroundDown+ height/3+7+(a+1)*30);
					a++;
					}
				}
				graph.dispose();
			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());

//		System.out.println(cout);
	}

	private String getTime() {
		String temps = new String(minute + ":" + seconde);
		if(playerlist.length >0) {
		if ( paused)
			return temps;
		milliseconde--;
		if (seconde == -1) {
			seconde = 59;
			minute--;
		}
		if (milliseconde == -14) {
			seconde--;
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
		if(snake!=null) {
			//judge the inputkeypress and add direction to the buffer
			for(int i =0;i<realPlayListOrder.length;i++) {
				playerlist[realPlayListOrder[i]].judgeInput(code);
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
	public void addRealPlaylistOrder(int a) {
		int[] neworderlist = new int[realPlayListOrder.length+1];
		System.arraycopy(realPlayListOrder, 0, neworderlist, 0, realPlayListOrder.length);
		neworderlist[neworderlist.length-1] = a;
		realPlayListOrder = neworderlist;
		System.out.println(realPlayListOrder.length);
	}
	
	
	
	
	public void setRealPlayerkeypress() {
		switch(realPlayListOrder.length){
			case 1:
				playerlist[realPlayListOrder[0]].setkeypress(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
				break;
			case 2:
				playerlist[realPlayListOrder[1]].setkeypress(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A);
				break;
			case 3:
				playerlist[realPlayListOrder[2]].setkeypress(KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_J);
				break;
			case 4:
				playerlist[realPlayListOrder[3]].setkeypress(KeyEvent.VK_G, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_V);
				break;
			default:
				break;
				
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
//	public static  ServerUIControl  () {
//		// TODO Auto-generated method stub
//
//		return serverUIControl;
//	}

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

