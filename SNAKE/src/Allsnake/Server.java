package Allsnake;
import javax.swing.JFrame;

import UIpackage.LoginUI;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Server extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1800, 1000);
		primaryStage.setScene(scene);
		
		LoginUI loginPage = new LoginUI();
		primaryStage.show();
	}
	
	
}