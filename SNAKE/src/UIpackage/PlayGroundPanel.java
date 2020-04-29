package UIpackage;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PlayGroundPanel extends JPanel {
	public PlayGroundPanel() {
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.gray);
	}
}
