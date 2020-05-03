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
		this.setBackground(Color.green);
		g.setColor(Color.blue);
		g.fillRect(0, 50, server.getWidth() + 7, server.getHeight() + 100);

		this.add(loginButton);
		
	}
}
