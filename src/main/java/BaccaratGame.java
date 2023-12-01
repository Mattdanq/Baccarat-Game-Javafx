import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import java.util.*;


public class BaccaratGame extends Application {

	public ArrayList<Card> playerHand, bankerHand;
	public BaccaratDealer theDealer;
	BaccaratGameLogic gameLogic;
	double currentBet;
	double totalWinnings;
	String playerDecision;
	int currentScene;
	int checkList, checkList2;
	int pPoints, bPoints;
	//buttons for starting again or leaving game
	Button menuPlay, options, freshStart, exit;
	//buttons for result decisions
	Button clear, drawButton, playerButton, bankerButton, confirm;
	TextField betInput;
	//images to represent the cards on the table
	String img1, img2, img3, img4, img5, img6, resultScreen, winnerScreen;
	Boolean menuOpen;
	Card dummy;
	//evaluates the money being moved around and how much
	public double evaluateWinnings() {
		String winner = gameLogic.whoWon(playerHand, bankerHand);
		double winnings = currentBet;
		if (playerDecision == winner && winner == "Draw") {
			winnings = currentBet * 8;;
			totalWinnings += currentBet * 8;
		} else if (playerDecision == winner) {
			winnings = currentBet;
			totalWinnings += currentBet;
		} else {
			totalWinnings -= currentBet;
		}
		return winnings;
	}
	
	public String nameFind(int value) {
		if (value == 11) {
			return "jack";
		} else if (value == 12) {
			return "queen";
		} else if (value == 13) {
			return "king";
		} else if (value == 1) {
			return "ace";
		} else if (value < 11) {
			return Integer.toString(value);
		} else {
			return "value needs to be below 14";
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		
		//setting important globals
		PauseTransition pause1 = new PauseTransition(Duration.seconds(3));
		PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
		PauseTransition pause3 = new PauseTransition(Duration.seconds(3));
		
		playerHand = new ArrayList<Card>();
		bankerHand = new ArrayList<Card>();
		theDealer = new BaccaratDealer();
		gameLogic = new BaccaratGameLogic();
		currentBet = 0;
		totalWinnings = 0;
		playerDecision = "";
		winnerScreen = "";
		resultScreen = "";
		currentScene = 0;
		menuOpen = false;
		img1 = "card_back.png";
		img2 = "card_back.png";
		img3 = "card_back.png";
		img4 = "card_back.png";
		img5 = "card_back.png";
		img6 = "card_back.png";
		//input textfield for betting screen
		betInput = new TextField("");
		betInput.setPromptText("Set an dollar bet amount here");
		betInput.setPrefWidth(400);
		
		//setting up buttons
		menuPlay = new Button("CLICK TO PLAY");
		menuPlay.setStyle("-fx-border-width: 3px; -fx-border-color: gold; -fx-font-size: 1.15em; -fx-text-fill: BLACK;");
		
		options = new Button("OPTIONS");
		options.setStyle("-fx-font-size: 2em; -fx-text-fill: BLACK;");
		options.setPrefWidth(200);
		options.setPrefHeight(30);
		
		freshStart = new Button("Fresh Start");
		freshStart.setStyle("-fx-text-fill: BLACK;");
		freshStart.setPrefWidth(200);
		freshStart.setPrefHeight(30);
		freshStart.setVisible(false);
		freshStart.setDisable(true);
		
		exit = new Button("exit");
		exit.setStyle("-fx-text-fill: BLACK;");
		exit.setPrefWidth(200);
		exit.setPrefHeight(30);
		exit.setVisible(false);
		exit.setDisable(true);
		
		clear = new Button("Clear Text");
		
		drawButton = new Button("Draw");
		drawButton.setStyle("-fx-border-width: 3px; -fx-border-color: gold; -fx-font-size: 1.15em; -fx-text-fill: BLACK;");
		
		playerButton = new Button("Player");
		playerButton.setStyle("-fx-border-width: 3px; -fx-border-color: gold; -fx-font-size: 1.15em; -fx-text-fill: BLACK;");
		
		bankerButton = new Button("Banker");
		bankerButton.setStyle("-fx-border-width: 3px; -fx-border-color: gold; -fx-font-size: 1.15em; -fx-text-fill: BLACK;");
		
		confirm = new Button("Confirm Choices");
		confirm.setStyle("-fx-border-width: 3px; -fx-border-color: gold; -fx-font-size: 1.15em; -fx-text-fill: BLACK;");
		confirm.setDisable(true);
		
		//this will check to see if a value for the money field is set and a button is picked for the result
		checkList = 0;
		checkList2 = 0;
		
		primaryStage.setTitle("BACCARAT GAME");
		
		//changes to the betting screen off the menu screen
		menuPlay.setOnAction(e -> primaryStage.setScene(bettingScene()));		
		
		//interatable for betInput textfield
		betInput.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.ENTER)){
				try {
					double value = Double.parseDouble(betInput.getText());
				} catch (NumberFormatException nfe) {
					betInput.setText("Incorrect, sent integer values only.");
				}
				double value = Double.parseDouble(betInput.getText());
				if (value < 0) {
					betInput.setText("Incorrect, bet needs to be above 0");
				} else {
					currentBet = Double.parseDouble(betInput.getText());
					betInput.setText("BET ACCEPTED. THANK YOU. YOU HAVE BET: " + currentBet);
					betInput.setEditable(false);
					checkList = 1;
					if (checkList2 == 1 && checkList == 1) {confirm.setDisable(false);}
					clear.setDisable(true);
				}
			}
		});
		
		//clears textfield
		clear.setOnAction(e -> {
			if (checkList != 1) {betInput.clear();}
		});
		
		//buttons to set playerDecision
		drawButton.setOnAction(e ->{
			playerDecision = "Draw"; 
			checkList2 = 1;	
			drawButton.setText("PICKED DRAW");
			playerButton.setText("Player");
			bankerButton.setText("Banker");
			if (checkList2 == 1 && checkList == 1) {confirm.setDisable(false);}
		});
		
		//Logic for the player result decision
		playerButton.setOnAction(e ->{
			playerDecision = "Player"; 
			checkList2 = 1;
			drawButton.setText("Draw");
			playerButton.setText("PICKED PLAYER");
			bankerButton.setText("Banker");
			if (checkList2 == 1 && checkList == 1) {confirm.setDisable(false);}
		});
		
		//Logic for the banker button result decision
		bankerButton.setOnAction(e ->{
			playerDecision = "Banker";
			checkList2 = 1;
			drawButton.setText("Draw");
			playerButton.setText("Player");
			bankerButton.setText("PICKED BANKER");
			if (checkList2 == 1 && checkList == 1) {confirm.setDisable(false);}
		});
		
		//first step of 3rd player card
		pause1.setOnFinished(e ->{
			//gets player extra card if applicable
			dummy = theDealer.drawOne();
			playerHand.add(dummy);
			img5 = nameFind(dummy.value) + "_of_" + dummy.suite + ".png";
			pPoints = gameLogic.handTotal(playerHand);
			bPoints = gameLogic.handTotal(bankerHand);
			//gets banker extra card if applicable
			 if (gameLogic.evaluateBankerDraw(bankerHand, dummy) == true) {
					primaryStage.setScene(cardScene());
					pause2.play();
				} else {
					String winnerRes = gameLogic.whoWon(playerHand, bankerHand);
					winnerScreen = winnerRes + " wins! ";
					evaluateWinnings();
					if (winnerRes == playerDecision) {resultScreen = "Congrats, you bet " + playerDecision + "! You win!";}
					else {resultScreen = "Sorry, you bet " + playerDecision + ". You lose.";}
					menuPlay.setDisable(false);
					primaryStage.setScene(cardScene());
					pause3.play();
				}
		});
		
		//first step of banker 3rd player card
		pause2.setOnFinished(e -> {
			bankerHand.add(theDealer.drawOne());
			img6 = nameFind(bankerHand.get(2).value) + "_of_" + bankerHand.get(2).suite + ".png";
			pPoints = gameLogic.handTotal(playerHand);
			bPoints = gameLogic.handTotal(bankerHand);
			
			String winnerRes = gameLogic.whoWon(playerHand, bankerHand);
			winnerScreen = winnerRes + " wins! ";
			evaluateWinnings();
			if (winnerRes == playerDecision) {resultScreen = "Congrats, you bet " + playerDecision + "! You win!";}
			else {resultScreen = "Sorry, you bet " + playerDecision + ". You lose.";}
			menuPlay.setDisable(false);
			pPoints = gameLogic.handTotal(playerHand);
			bPoints = gameLogic.handTotal(bankerHand);
			primaryStage.setScene(cardScene());
			pause3.play();
		});
		
		//results pause and sets up for player to bet again
		pause3.setOnFinished(e -> {
			confirm.setDisable(true);
			betInput.setEditable(true);
			betInput.clear();
			clear.setDisable(false);
			checkList = 0;
			checkList2 = 0;
			winnerScreen = "";
			resultScreen = "";
			drawButton.setText("Draw");
			playerButton.setText("Player");
			bankerButton.setText("Banker");
			menuPlay.setText("Click to Play Again");
			img5 = "card_back.png";
			img6 = "card_back.png";
		});
		
		//leading event that does most of the work in the card scene
		confirm.setOnAction(e -> {			
			theDealer.shuffleDeck();
			//theDealer.generateDeck();
			playerHand = theDealer.dealHand();
			bankerHand = theDealer.dealHand();
			//gets images for player hand
			for (int i = 0; i < 2; i++) {
				Card holder = playerHand.get(i);
				if (i == 0) {
					img1 =  nameFind(holder.value) + "_of_" + holder.suite + ".png";
				} else {
					img2 =  nameFind(holder.value) + "_of_" + holder.suite + ".png";
				}
			}
			//gets images for banker hand
			for (int i = 0; i < 2; i++) {
				Card holder = bankerHand.get(i);
				if (i == 0) {
					img3 =  nameFind(holder.value) + "_of_" + holder.suite + ".png";
				} else {
					img4 =  nameFind(holder.value) + "_of_" + holder.suite + ".png";
				}
			}
			
			//checks if the the 3rd card will be possible along with checking for a natural win right off the bat
			dummy = new Card("fake", -1);
			menuPlay.setDisable(true);
			pPoints = gameLogic.handTotal(playerHand);
			bPoints = gameLogic.handTotal(bankerHand);
			//checks for natural win
			if ((pPoints == 9 || pPoints == 8) || (bPoints == 9 || bPoints == 8)) {
				evaluateWinnings();
				String winnerRes = gameLogic.whoWon(playerHand, bankerHand);
				winnerScreen = winnerRes + " wins! ";
				if (winnerRes == playerDecision) {resultScreen = "Congrats, you bet " + playerDecision + "! You win!";}
				else {resultScreen = "Sorry, you bet " + playerDecision + ". You lose.";}
				menuPlay.setDisable(false);
				primaryStage.setScene(cardScene());
				pause3.play();
				//if player gets 3rd card
			} else if (gameLogic.evaluatePlayerDraw(playerHand) == true) {
				primaryStage.setScene(cardScene());
				pause1.play();
				//if banker gets another card
			} else if (gameLogic.evaluateBankerDraw(bankerHand, dummy) == true) {
				primaryStage.setScene(cardScene());
				pause2.play();
				//if both do not get another card
			} else {
				evaluateWinnings();
				String winnerRes = gameLogic.whoWon(playerHand, bankerHand);
				winnerScreen = winnerRes + " wins! ";
				if (winnerRes == playerDecision) {resultScreen = "Congrats, you bet " + playerDecision + "! You win!";}
				else {resultScreen = "Sorry, you bet " + playerDecision + ". You lose.";}
				menuPlay.setDisable(false);
				primaryStage.setScene(cardScene());
				pause3.play();
			}
		});
		
		//options tab
		options.setOnAction(e ->{
			if (menuOpen == false) {
				menuOpen = true;

				freshStart.setVisible(true);
				freshStart.setDisable(false);
				exit.setVisible(true);
				exit.setDisable(false);		
			} else {
				menuOpen = false;
				
				freshStart.setVisible(false);
				freshStart.setDisable(true);
				exit.setVisible(false);
				exit.setDisable(true);
			}
		});
		
		//makes the game anew, no more debt
		freshStart.setOnAction(e ->{
			currentBet = 0;
			totalWinnings = 0;
			confirm.setDisable(true);
			betInput.setEditable(true);
			clear.setDisable(false);
			checkList = 0;
			checkList2 = 0;
			winnerScreen = "";
			resultScreen = "";
			drawButton.setText("Draw");
			playerButton.setText("Player");
			bankerButton.setText("Banker");
			menuPlay.setText("Click to Play Again");
			img5 = "card_back.png";
			img6 = "card_back.png";
			primaryStage.setScene(bettingScene());
		});
		
		//exits game
		exit.setOnAction(e -> Platform.exit());
		
		//sets up hands
		playerHand = new ArrayList<Card>();
		bankerHand = new ArrayList<Card>();
		
		primaryStage.setScene(createPlayScene());
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	//creates game menu screen
	public Scene createPlayScene() {
		
		menuPlay.setMaxSize(400, 300);
		
		Text baccaratScreen = new Text("Baccarat");
		baccaratScreen.setFont(Font.font("Comic Sans", FontWeight.BOLD, 155));
		baccaratScreen.setFill(Color.BLACK);
		baccaratScreen.setStyle("-fx-fill: black; -fx-stroke: gold; -fx-stroke-width: 3px;");
		
		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-image: url(redwallpaper.jpg); -fx-background-size: cover;");
		pane.setCenter(baccaratScreen);
		pane.setBottom(menuPlay);
		pane.setAlignment(menuPlay, Pos.BASELINE_CENTER);
		pane.setPadding(new Insets(0,0,200,0));
		return new Scene(pane, 1600, 800);
	}
	
	public Scene bettingScene() {
		
		VBox menuBar = new VBox(10, freshStart, exit);
		VBox topBar = new VBox(5, options, menuBar);
		
		String moneyS = "Current Winnings " + totalWinnings;
		Text moneySoFar = new Text(moneyS);
		moneySoFar.setFont(Font.font("Comic Sans", FontWeight.BOLD, 50));
		moneySoFar.setFill(Color.BLACK);
		moneySoFar.setStyle("-fx-fill: black; -fx-stroke: darkgreen; -fx-stroke-width: 1.1px;");
		
		Text betMessage = new Text("Set your bet below.");
		betMessage.setFont(Font.font("Comic Sans", FontWeight.BOLD, 50));
		betMessage.setFill(Color.BLACK);
		betMessage.setStyle("-fx-fill: black; -fx-stroke: gold; -fx-stroke-width: 0.9px;");
		
		Text decideMessage = new Text("Pick a result to bet on.");
		decideMessage.setFont(Font.font("Comic Sans", FontWeight.BOLD, 50));
		decideMessage.setFill(Color.BLACK);
		decideMessage.setStyle("-fx-fill: black; -fx-stroke: gold; -fx-stroke-width: 0.9px;");
		
		playerButton.setMaxSize(600, 300);
		drawButton.setMaxSize(600, 300);
		bankerButton.setMaxSize(600, 300);
		confirm.setMaxSize(600, 300);
		
		HBox betSection = new HBox(10, betInput, clear);
		VBox betBox = new VBox(20, moneySoFar, betMessage, betSection);
		VBox decideBox = new VBox(20, decideMessage, drawButton, playerButton, bankerButton, confirm);
		VBox fullBettingScreen = new VBox(10, betBox, decideBox);
		BorderPane pane = new BorderPane();
		pane.setTop(topBar);
		pane.setCenter(fullBettingScreen);
		fullBettingScreen.setPadding(new Insets(40, 500, 10, 500));
		pane.setStyle("-fx-background-image: url(redwallpaper.jpg); -fx-background-size: cover;");
		return new Scene(pane, 1600, 800);
	}

	public Scene cardScene() {
		Text betMoney = new Text("Amount Bet: " + currentBet);
		betMoney.setFont(Font.font("Comic Sans", FontWeight.BOLD, 42.5));
		betMoney.setFill(Color.BLACK);
		betMoney.setStyle("-fx-fill: black; -fx-stroke: darkgreen; -fx-stroke-width: 1.1px;");
		
		Text curWin = new Text("Current Winnings: " + totalWinnings);
		curWin.setFont(Font.font("Comic Sans", FontWeight.BOLD, 42.5));
		curWin.setFill(Color.BLACK);
		curWin.setStyle("-fx-fill: black; -fx-stroke: darkgreen; -fx-stroke-width: 1.1px;");
		
		//makes overhead bar
		HBox topBSec1 = new HBox(20, options, betMoney);
		HBox topBSec2 = new HBox(200, topBSec1, curWin);
		VBox menuBar = new VBox(10, freshStart, exit);
		VBox topBar = new VBox(5, topBSec2, menuBar);
		
		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-image: url(redwallpaper.jpg); -fx-background-size: cover;");
		
		Image pC1 = new Image(img1);
		ImageView card1 = new ImageView(pC1);
		card1.setPreserveRatio(true);
		card1.setFitHeight(400);
		card1.setFitWidth(150);
		
		Image pC2 = new Image(img2);
		ImageView card2 = new ImageView(pC2);
		card2.setPreserveRatio(true);
		card2.setFitHeight(400);
		card2.setFitWidth(150);
		
		Image pC3 = new Image(img5);
		ImageView card3 = new ImageView(pC3);
		card3.setPreserveRatio(true);
		card3.setFitHeight(400);
		card3.setFitWidth(150);
		
		Image bC1 = new Image(img3);
		ImageView card4 = new ImageView(bC1);
		card4.setPreserveRatio(true);
		card4.setFitHeight(400);
		card4.setFitWidth(150);
		
		Image bC2 = new Image(img4);
		ImageView card5 = new ImageView(bC2);
		card5.setPreserveRatio(true);
		card5.setFitHeight(400);
		card5.setFitWidth(150);
		
		Image bC3 = new Image(img6);
		ImageView card6 = new ImageView(bC3);
		card6.setPreserveRatio(true);
		card6.setFitHeight(400);
		card6.setFitWidth(150);
		
		Text playerPoints = new Text("Player Hand Total: " + pPoints);
		playerPoints.setFont(Font.font("Comic Sans", FontWeight.BOLD, 50));
		playerPoints.setFill(Color.BLACK);
		playerPoints.setStyle("-fx-fill: black; -fx-stroke: gold; -fx-stroke-width: 0.9px;");
		
		Text bankerPoints = new Text("Banker Hand Total: " + bPoints);
		bankerPoints.setFont(Font.font("Comic Sans", FontWeight.BOLD, 50));
		bankerPoints.setFill(Color.BLACK);
		bankerPoints.setStyle("-fx-fill: black; -fx-stroke: gold; -fx-stroke-width: 0.9px;");
		
		HBox imagePlayer = new HBox(50, card1, card2, card3);
		HBox imageBanker = new HBox(50, card4, card5, card6);
		HBox imageBox = new HBox(90, imagePlayer, imageBanker);
		imageBox.setPadding(new Insets(100, 0, 10, 200));
		
		HBox pointsBox = new HBox(140, playerPoints, bankerPoints);
		pointsBox.setPadding(new Insets(10, 0, 10, 250));
		
		Text result = new Text(winnerScreen + resultScreen);
		result.setFont(Font.font("Comic Sans", FontWeight.BOLD, 50));
		result.setFill(Color.BLACK);
		result.setStyle("-fx-fill: black; -fx-stroke: gold; -fx-stroke-width: 0.9px;");
		
		VBox resultsBox = new VBox(10, result);
		resultsBox.setPadding(new Insets(10, 0, 10, 300));
		
		VBox centerArea = new VBox(10, imageBox, pointsBox, resultsBox);
		pane.setTop(topBar);
		pane.setCenter(centerArea);
		pane.setBottom(menuPlay);
		pane.setAlignment(centerArea, Pos.TOP_CENTER);
		pane.setAlignment(menuPlay, Pos.BASELINE_CENTER);
		return new Scene(pane, 1600, 800);
	}
}
