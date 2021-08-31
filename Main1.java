/**
 * CS 501 Introduction to JAVA Programming 
 * M. Jurkat
 * Final Project: Connect Four Game
 * This project is based on Ch8-20
 * Deng Xiong
 * Mainly studied from Ch8, Ch14, Ch15
 * Textbook:12th
 */

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
//import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main1 extends Application{	
	
	//Initialize
	//Pane parameters
	public GridPane rootNode = new GridPane();
	public BorderPane rootPane = new BorderPane();
	public GridPane gameTable = new GridPane();	
	public FlowPane buttons = new FlowPane();
	StackPane centerPane = new StackPane();				
	//Check parameters
	public boolean goRed = true;
	public boolean winner = false;
	//Table parameters
	public int columns = 7, rows = 6, circles = 65, rad = 25, turn= 1;
	public Circle[][] disk = new Circle[rows][columns];	
	public Rectangle tRee = new Rectangle(columns*circles, rows*circles); 
	public Text champ = new Text(); 
	public Text tuRn = new Text();	
	//Players
	//public String r = "RED", y = "YELLOW";
	public String r, y;
	
	 public String getPlayerR(){
   		return this.r;
		}
		
	 public String getPlayerY(){
	 	return this.y;
		}
	 
	
	@Override
	public void start(Stage stageL) { 
		
		//PrimaryStage for players to log in with their names		
		stageL.setTitle("Let's play");
		
        rootNode.setPadding(new Insets(15));
        rootNode.setHgap(5);
        rootNode.setVgap(5);
        rootNode.setAlignment(Pos.CENTER);

        Scene sceneL = new Scene(rootNode, 300, 200);

        rootNode.add(new Label("Red player name:"), 0, 0);
        TextField firstPlayer = new TextField();
        rootNode.add(firstPlayer, 1, 0);
        rootNode.add(new Label("Yellow player name:"), 0, 1);
        TextField secondPlayer = new TextField();
        rootNode.add(secondPlayer, 1, 1);        
        Button startButton = new Button("Start Game");
        rootNode.add(startButton, 1, 2);
        GridPane.setHalignment(startButton, HPos.LEFT);
        TextField result = new TextField();
        result.setEditable(false);        
        
        //User can check the names in console 
        //Switch scenes
        startButton.setOnAction(e -> {
        	//Player Red
            //String r = String.valueOf(firstPlayer.getText());
            //Player Yellow
           //String y = String.valueOf(secondPlayer.getText());    
            
             
   					this.r = String.valueOf(firstPlayer.getText());
   					this.y = String.valueOf(secondPlayer.getText());
				
            
            
            //Get their Names           
            System.out.println("RED player is " + r);
            System.out.println("YELLOW player is " + y);  
            stageL.hide();
            //gameStop(r, y);
            generaTable();         
       
            //Gaming stage for connecting game
            Stage gameStage = new Stage();            
    		List<Rectangle> showCols = generaClick();	    		
    		Button resetButton = new Button("Play Again");	
    		Button quitButton = new Button("Quit Game");
    		
    		//Button resetButton, quitButton
    		buttons.getChildren().addAll(resetButton, quitButton);
    		buttons.setAlignment(Pos.BOTTOM_CENTER);
    		buttons.setPadding(new Insets(10));
    		buttons.setHgap(80);
    		
    		//resetButton
    		resetButton.setAlignment(Pos.BOTTOM_LEFT);
    		resetButton.setPadding(new Insets(10, 10, 10, 10));
    		resetButton.setFont(new Font("Times New Roman", 20));
    		
    		//quitButton
    		quitButton.setAlignment(Pos.BOTTOM_RIGHT);
    		quitButton.setPadding(new Insets(10, 10, 10, 10));
    		quitButton.setFont(new Font("Times New Roman", 20));
    		
    		gameTable.setAlignment(Pos.CENTER);    		    		
    		//generaTable(r, y);
    		//deleteBoard();
    		
    		//show winner
    		tRee.setFill(Color.TRANSPARENT);
    		champ.setFont(new Font("Times New Roman", 70));
    		champ.setFill(Color.TRANSPARENT);
    		
    		//Pane table
    		centerPane.getChildren().add(gameTable);
    		centerPane.getChildren().add(tRee);
    		centerPane.getChildren().add(champ);
    		for(Rectangle x : showCols)
    			centerPane.getChildren().add(x);
    			centerPane.setPadding(new Insets(0,0,10,0));	
    		
    		//Place text
    		rootPane.setCenter(centerPane);
    		rootPane.setBottom(buttons);		
    		rootPane.setTop(tuRn);    		
    		
    		//Buttons
    		resetButton.setOnAction(new GoResetor());
    		quitButton.setOnAction(event -> gameStage.hide());    		
    		
    		//Scene
    		Scene scene = new Scene(rootPane);
    		gameStage.setTitle("Game:connect four");
    		gameStage.setScene(scene);
    		gameStage.show();
    		
    		//Logout option
    		gameStage.setOnCloseRequest(event -> {
    			event.consume();
    			logout(gameStage);
    		});
            
        });
            	        
        stageL.setScene(sceneL);
        stageL.show();		                	      
    	
    	
	} 
	
	//Log out Alert for the dialog
	public void logout(Stage stage)
	{
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quit");
		alert.setHeaderText("You are about to quit the game!");
		alert.setContentText("Do you want to quit?:");
		
		if(alert.showAndWait().get() == ButtonType.OK) {		
			stage.close();
		}
	}
	
	
	//Switch
	public void switchPlayer() {
		if(goRed == true) {
			tuRn.setText(r + "'s " + "Move"); 
			tuRn.setFill(Color.BLACK);
		}
		else {
			tuRn.setText(y + "'s " + "Move");
			tuRn.setFill(Color.BLACK);
		}
	}
		
	// Create Table
	public void generaTable() {
		goRed = true;
		centerPane.setDisable(false);
		tRee.setFill(Color.TRANSPARENT);
		champ.setFill(Color.TRANSPARENT);
		champ.setStroke(Color.TRANSPARENT);
		tuRn.setFont(new Font("Times New Roman", 40));
		tuRn.setStroke(Color.WHITE);
		switchPlayer();
		gameTable.getChildren().clear();
		
		for(int a = 0; a < rows; a++) {
			for(int b = 0; b < columns; b++) {
				Rectangle tile = new Rectangle(circles, circles);
				Circle circle = new Circle(rad);								
				circle.centerXProperty().set(circles/2);
				circle.centerYProperty().set(circles/2);								
				Shape cell = Path.subtract(tile, circle);
				cell.setFill(Color.GREEN.darker());				
				//Array
				disk[a][b] = new Circle(rad, Color.WHITE);
				disk[a][b].setStroke(Color.BLACK);
				StackPane stack = new StackPane();
				stack.getChildren().addAll(cell,disk[a][b]);							
				gameTable.add(stack, b, a);
			}
		}
	}
	
	// User select (start only from the bottom)
	public List<Rectangle> generaClick() {
		List<Rectangle> cols = new ArrayList<>();		
		int s = circles * rows;		
		int x = 3 * circles;
		int c = 0;
		while ( c < columns) {
			Rectangle show_col = new Rectangle(circles, s);
			show_col.setTranslateX(circles * c - x);
			show_col.setFill(Color.TRANSPARENT);						
			show_col.setOnMouseEntered(e -> show_col.setFill(Color.TRANSPARENT));						
			show_col.setOnMouseExited(e -> show_col.setFill(Color.TRANSPARENT));
			show_col.setOnMouseClicked(new GoSelecter(c));			
			cols.add(show_col);
			c++;			
		}
		return cols;			
		
	}
	
	
	/*
	 * //Quit game
	 *	public void quitGame() { 
	 *	
	 *		stage = gameStage
	 *		stage.close();
		
	}
	 */
	
/*
 * Check the connects
 * a stands for add
 * s stands for sub
 * c, col stands for columns
 * dia stands for diagonal
 * r stands for rows
 */
	public boolean add2row(Paint player, int row, int col) {
    	
    	if(row + 1 < rows && disk[row+1][col].getFill() == player)
    		if(row + 2 < rows && disk[row+2][col].getFill() == player)
    			if(row + 3 < rows && disk[row+3][col].getFill() == player)
        			return true;
    	return false;
	}   
    public boolean sub4row(Paint player, int row, int col) {
    	
    	if(row - 1 > -1 && disk[row-1][col].getFill() == player)
    		if(row - 2 > -1 && disk[row-2][col].getFill() == player)
    			if(row - 3 > -1 && disk[row-3][col].getFill() == player)
        			return true;
    	return false;
    }  
    public boolean sub4col(Paint player, int row, int col) {
    	
    	if(col - 1 > -1 && disk[row][col-1].getFill() == player)
    		if(col - 2 > -1 && disk[row][col-2].getFill() == player)
    			if(col - 3 > -1 && disk[row][col-3].getFill() == player)
        			return true;
    	return false;
    }   
    public boolean add2col(Paint player, int row, int col) {
    	
    	if(col + 1 < columns && disk[row][col+1].getFill() == player)
    		if(col + 2 < columns && disk[row][col+2].getFill() == player)
    			if(col + 3 < columns && disk[row][col+3].getFill() == player)
        			return true;
    	return false;
    }      
    public boolean sub4dia(Paint player, int row, int col) {
    	
    	if(col - 1 > -1 && row - 1 > -1 && disk[row-1][col-1].getFill() == player)
    		if(col - 2 > -1 && row - 2 > -1 && disk[row-2][col-2].getFill() == player)
    			if(col - 3 > -1 && row - 3 > -1 && disk[row-3][col-3].getFill() == player)
        			return true;
    	return false;
    }       
    public boolean sub4r0c(Paint player, int row, int col) {
    
    	if(col + 1 < columns && row - 1 > -1 && disk[row-1][col+1].getFill() == player)
    		if(col + 2 < columns && row - 2 > -1 && disk[row-2][col+2].getFill() == player)
    			if(col + 3 < columns && row - 3 > -1 && disk[row-3][col+3].getFill() == player)
        			return true;
    	return false;
    }       
    public boolean add4r0c(Paint player, int row, int col) {
    	
    	if(col + 1 < columns && row + 1 < rows && disk[row+1][col+1].getFill() == player)
    		if(col + 2 < columns && row + 2 < rows && disk[row+2][col+2].getFill() == player)
    			if(col + 3 < columns && row + 3 < rows && disk[row+3][col+3].getFill() == player)
        			return true;
    	return false;
    }       
    public boolean a4rs4c(Paint player, int row, int col) {
    	
    	if(col - 1 > -1 && row + 1 < rows && disk[row+1][col-1].getFill() == player)
    		if(col - 2 > -1 && row + 2 < rows && disk[row+2][col-2].getFill() == player)
    			if(col - 3 > -1 && row + 3 < rows && disk[row+3][col-3].getFill() == player)
        			return true;
    	return false;
    }   
    
	    
    //is the player win
	public boolean isheWin(int row, int col) {
		Paint playerColor = disk[row][col].getFill();
		boolean winner = false;
		int a = 0; 			
    	while (a < rows)
        {
    		int b = 0;
            while(b < columns)
            {
            	if(disk[a][b].getFill() == playerColor)
            	{
	            	winner |= add2row(playerColor, a, b);
	            	winner |= sub4row(playerColor, a, b);
	            	winner |= sub4col(playerColor, a, b);
	            	winner |= add2col(playerColor, a, b);
	            	winner |= sub4dia(playerColor, a, b);
	            	winner |= sub4r0c(playerColor, a, b);
	            	winner |= add4r0c(playerColor, a, b);
	            	winner |= a4rs4c(playerColor, a, b);
            	}            	            	
            	b++;         
            }
            a++;     
        }
    	return winner; 
    	
    	
		
	}
	
	//Stop the game
		public void gameStop(int a) { 
			//determine if there is a winner

//			System.out.println("cols= " + winner);
			tRee.setFill(Color.WHITE);
			//Paint fplayerColor = disk[6][7].getFill();
			
			if(goRed == true) {
				champ.setText(r + " wins!"); 
				champ.setFill(Color.BLACK);
				champ.setStroke(Color.BLACK);
			
//			}else if (turn== 42){
//				champ.setText("Tie game");
//				champ.setFill(Color.BLACK);
//				champ.setStroke(Color.BLACK);
			}
			else {
				champ.setText(y + " wins!");
				champ.setFill(Color.BLACK);
				champ.setStroke(Color.BLACK);
			}
				centerPane.setDisable(true);
		}
		
	
	//Cat is the chose col
	class GoSelecter implements EventHandler<MouseEvent> {
		public int cat;
		
		
		//location of the player move
		public GoSelecter(int col) {
			this.cat = col;
		}
		
		@Override
		public void handle(MouseEvent event) {
			int t = 2;			
				
				for(int a = rows-1; a >= 0; a--) {	
					if(disk[a][cat].getFill() == Color.WHITE) {
						//Fills bottom white disc of selected column with color of current player
						disk[a][cat].setFill(goRed == true ? Color.RED : Color.YELLOW);				
						//See if player has won, if so set the gameStop conditions
						if(isheWin(a, cat)) 
							gameStop(a);						
						//Switch to other players turn
						goRed = !goRed;	
						//Display the new players turn to user
						switchPlayer();	
						//Exit looping
						a = t;
						break;		
						
					}
//					System.out.println("tie  ");
//					System.out.println("a=   " +a);
//					System.out.println("cat=  "+cat);
					if(a==1&&cat==6) {
						tRee.setFill(Color.WHITE);
						champ.setText("Tie game");
						champ.setFill(Color.BLACK);
						champ.setStroke(Color.BLACK);
					}
					
				}	
				
			}
		
	}
	
	
	//Reset for the reset button
	class GoResetor implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e)
		{
			generaTable();
		}
	}
	
/*
 * When quitButton is pressed, generaTable is shut down
 * class GoQuiter implements EventHandler<ActionEvent> {
 * @Override
 * public void handle(ActionEvent e)
 * {
			
				quitGame();
			
		}
		}
 */

	//Launch application	
	public static void main(String[] args) {
		Application.launch(args);
	}

}