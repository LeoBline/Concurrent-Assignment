package Allsnake;
import UIpackage.PlayGroundPanel;
import javax.swing.JFrame;

public class Server {

	//window for the game
	static JFrame frame = new JFrame();
	//panel on the window
	static PlayGroundPanel playGrand = new PlayGroundPanel();
	public static void main(String[] args) {
		initmap();


	}

	private static  void initmap() {
		frame.setBounds(20, 0, 1000, 820);
		frame.setResizable(false);//cannot adjust size of screen
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//set close button for the window

		frame.add(playGrand);
		frame.setVisible(true);
	}
}