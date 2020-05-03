package Allsnake;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScoreBoard extends JFrame{
	
private static final long serialVersionUID = 1L;
    
    // Define components
    JPanel jp1, jp2, jp3;
    JButton jb1, jb2, jb3, jb4, jb5, jb6;

    public static void main(String[] args) {
        new ScoreBoard();
    }

    public ScoreBoard() {
        // Create component
        
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        jb1 = new JButton("Player 1");
        jb2 = new JButton("Score");
        jb3 = new JButton("Player 2");
        jb4 = new JButton("Score");
        jb5 = new JButton("Player 3");
        jb6 = new JButton("Score");

        // Set Layout Manager (Jpanel default flow layout)
        jp1.add(jb1);
        jp1.add(jb2);
        jp2.add(jb3);
        jp2.add(jb4);
        jp3.add(jb5);
        jp3.add(jb6);

        // Add Panel to JFrame
        this.add(jp1, BorderLayout.NORTH);
        this.add(jb3, BorderLayout.CENTER);
        this.add(jp2, BorderLayout.SOUTH);

        // Set form
        // Form size
        this.setSize(400, 300);
        // Screen display initial position
        this.setLocation(300, 300);
        // display
        this.setVisible(true);
        // Close the JFrame after exiting the form
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


