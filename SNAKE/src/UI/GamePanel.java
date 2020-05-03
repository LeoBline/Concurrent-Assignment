package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.*;

import javax.swing.JButton;
import javax.swing.JPanel;

import Allsnake.Server;

public class GamePanel extends JPanel{
	Server server = Server.getSever();
	
	private JButton loginButton = new JButton("login");
	
	public GamePanel() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		this.setBackground(Color.green);
//		g.fillRect(10, 50, server.getWidth(), server.getHeight());
		
		loginButton.setLocation(500, 500);
		this.add(loginButton);
		
	}
}
