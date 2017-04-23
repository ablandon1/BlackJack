import java.awt.Toolkit;
import java.io.File;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * Game's logic and UI
 *
 * @author Andres Blandon
 * @created 4/17/17
 * 
 * Sounds are not working
 */
public class TwentyOne extends Application {

    private Deck deck = new Deck();
    private Hand dealer, player;
    private Text message = new Text();
    private int playerWins = 0;
    private int dealerWins = 0;

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox playerCards = new HBox(20);
    
    
    private Parent createGame() {
        
    	titlePage();
    	
    	dealer = new Hand(dealerCards.getChildren());
        player = new Hand(playerCards.getChildren());
        
        Pane root = new Pane();
        root.setPrefSize(800, 600);
        
        Region background = new Region();
        background.setPrefSize(800, 600);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        
        Rectangle leftBG = new Rectangle(550, 560);
        leftBG.setArcWidth(50);
        leftBG.setArcHeight(50);
        leftBG.setFill(Color.DARKGREEN);
        
        Rectangle rightBG = new Rectangle(230, 560);
        rightBG.setArcWidth(50);
        rightBG.setArcHeight(50);
        rightBG.setFill(Color.RED);

        // Left Game Space
        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text dealerScore = new Text("Dealer: ");
        Text playerScore = new Text("Player: ");

        leftVBox.getChildren().addAll(dealerScore, dealerCards, message, playerCards, playerScore);

        // Right Game Space

        VBox rightVBox = new VBox(20);
        rightVBox.setAlignment(Pos.CENTER);        
       
        //unable to update the amount of wins for each hand when the round is over
        Text dealerFinalScore = new Text("Dealer Score: " + playerWins);
        Text playerFinalScore = new Text("Player Score: " + dealerWins);
        

        Button btnShuffle = new Button("SHUFFLE");
        Button btnHit = new Button("HIT");
        Button btnStay = new Button("STAY");

        HBox buttonsHBox = new HBox(15, btnHit, btnStay);
        buttonsHBox.setAlignment(Pos.CENTER);

        rightVBox.getChildren().addAll(playerFinalScore,dealerFinalScore,btnShuffle, buttonsHBox);


        rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(rightBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);


        btnShuffle.disableProperty().bind(playable);
        btnHit.disableProperty().bind(playable.not());
        btnStay.disableProperty().bind(playable.not());

        playerScore.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.valueProperty().asString()));
        dealerScore.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.valueProperty().asString()));

        player.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

        dealer.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
            }     
        });

        //button listeners

        btnShuffle.setOnAction(event -> {
            startNewGame();
        	shuffleSound();

        });

        btnHit.setOnAction(event -> {player.takeCard(deck.drawCard()); });

        btnStay.setOnAction(event -> {
            while (dealer.valueProperty().get() < 21) {
                dealer.takeCard(deck.drawCard());
            }
            endGame();
        });
		
        return root;   
       
    }

    private void startNewGame() {
        playable.set(true);
        message.setText("");

        deck.refill();

        dealer.reset();
        player.reset();

        dealer.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
    }

    private void endGame() {
        playable.set(false);

        int dealerValue = dealer.valueProperty().get();
        int playerValue = player.valueProperty().get();
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue;
        
        
        if (dealerValue == 21 || playerValue > 21 || dealerValue == playerValue
                || (dealerValue < 21 && dealerValue > playerValue)) {
            winner = "DEALER";
        	playBadSound();
            dealerWins++;

        }
        else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
            winner = "PLAYER";
            playGoodSound();
            playerWins++;

            
        }

        message.setText(winner + " WON");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
       primaryStage.setScene(new Scene(createGame()));
       primaryStage.setWidth(800);
       primaryStage.setHeight(600);
       primaryStage.setResizable(false);
       primaryStage.setTitle("Black Jack");
       primaryStage.show();
    }

   //title page (unable to work)
    private StackPane titlePage(){
    	
    	StackPane pane = new StackPane();
    	pane.setPrefSize(800,600);
        Image img = new Image("https://www.gentingcasino.com/images/uploads/games/blackjack.jpg");
        
        ImageView titleImg = new ImageView(img);
        titleImg.setFitWidth(800);
        titleImg.setFitHeight(600);


        HBox titleLayout = new HBox(5);
        titleLayout.setPadding(new Insets(5, 5, 5, 5));
        Button startBtn = new Button("START GAME");
        TextField userName = new TextField();
        userName.setPromptText("Enter your player name:");
        userName.getText();
        
        titleLayout.setAlignment(Pos.BOTTOM_CENTER);
        titleLayout.getChildren().addAll(titleImg,startBtn);
		return pane;
        

    }
    
    
    //sounds and media player instance
    public void playGoodSound(){
    	
    	Media goodSound = new Media(new File("GoodSound.mp3").toURI().toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(goodSound);
    	mediaPlayer.play();
       
    }
    public void playBadSound(){
        
    	Media badSound = new Media(new File("BadSound.mp3").toURI().toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(badSound);
    	mediaPlayer.play();
   
  }
    public void playLosingSound(){
        
    	Media losingSound = new Media(new File("LosingSound.mp3").toURI().toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(losingSound);
    	mediaPlayer.play();
  }

    public void playWinSound(){
        
    	Media winningSound = new Media(new File("WinningSound.mp3").toURI().toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(winningSound);
    	mediaPlayer.play();
   
  }
    public void shuffleSound(){
        
    	Media shuffle = new Media(new File("shuffle.mp3").toURI().toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(shuffle);
    	mediaPlayer.play();
   
  }
  
    public static  void main(String[] args) {
        launch(args);
    }
}