package Allsnake;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PlayGroundPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayGroundPanel() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.gray);
	}
}
