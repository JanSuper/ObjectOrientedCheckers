package ObjectUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;


public class StartUI extends Application{

    public static boolean playerOneAI;
    public static boolean playerTwoAI;
    public static int mcLIMIT;
    public static boolean mctsWithRave;
    public static int AIone;
    public static int AItwo;
    
    public static Main singleton = null;

    public Stage primaryStage = new Stage();
    Label depth1 = new Label();
    Label depth2 = new Label();
    @Override
    public void start(Stage stage) throws Exception {

        Pane pane = new Pane();
        Label player1 = new Label("Player 1:");
        player1.setTranslateX(50);
        player1.setTranslateY(100);

        pane.getChildren().add(player1);

        Label player2 = new Label("Player 2:");
        player2.setTranslateX(340);
        player2.setTranslateY(100);

        pane.getChildren().add(player2);

        ComboBox choicesPlayer1 = new ComboBox();
        choicesPlayer1.getItems().addAll(
                "Player 1",
                "MCTS",
                "MCTS with rave",
                "MINIMAX",
                "MINIMAX with pruning");
        choicesPlayer1.setPrefWidth(200);
        choicesPlayer1.setTranslateX(120);
        choicesPlayer1.setTranslateY(100);
        pane.getChildren().add(choicesPlayer1);

        TextField depthValue1 = new TextField();
        choicesPlayer1.setOnAction(( e ->{
            if(choicesPlayer1.getValue().equals("MINIMAX with pruning") || choicesPlayer1.getValue().equals("MINIMAX")){
                depth1.setText("Depth of the tree: ");
                depth1.setTranslateX(120);
                depth1.setTranslateY(140);
                depthValue1.setTranslateX(220);
                depthValue1.setTranslateY(140);
                depthValue1.setPrefWidth(40);
                if(!pane.getChildren().contains(depthValue1))
                    pane.getChildren().add(depthValue1);
            }else if(choicesPlayer1.getValue().equals("MCTS") || choicesPlayer1.getValue().equals("MCTS with rave")){
                depth1.setText("Stop condition (in ms) :");
                depth1.setTranslateX(120);
                depth1.setTranslateY(140);
                depthValue1.setTranslateX(240);
                depthValue1.setTranslateY(140);
                depthValue1.setPrefWidth(40);


                pane.getChildren().add(depthValue1);
            }
        }));
        pane.getChildren().add(depth1);


        ComboBox choicesPlayer2 = new ComboBox();
        choicesPlayer2.getItems().addAll(
                "Player 2",
                "MCTS",
                "MCTS with rave",
                "MINIMAX",
                "MINIMAX with pruning");
        choicesPlayer2.setPrefWidth(200);
        choicesPlayer2.setTranslateX(400);
        choicesPlayer2.setTranslateY(100);
        pane.getChildren().add(choicesPlayer2);

        TextField depthValue2 = new TextField();
        choicesPlayer2.setOnAction( e ->{
            if(choicesPlayer2.getValue().equals("MINIMAX with pruning") || choicesPlayer2.getValue().equals("MINIMAX")){
                depth2.setText("Depth of the tree: ");
                depth2.setTranslateX(340);
                depth2.setTranslateY(140);
                depthValue2.setTranslateX(440);
                depthValue2.setTranslateY(140);
                depthValue2.setPrefWidth(40);

                pane.getChildren().add(depthValue2);
            }else if(choicesPlayer2.getValue().equals("MCTS") || choicesPlayer2.getValue().equals("MCTS with rave")){
                depth2.setText("Stop condition (in ms) :");
                depth2.setTranslateX(340);
                depth2.setTranslateY(140);
                depthValue2.setTranslateX(460);
                depthValue2.setTranslateY(140);
                depthValue2.setPrefWidth(40);
                if(!pane.getChildren().contains(depthValue2))
                    pane.getChildren().add(depthValue2);
            }
        });

        Button btn=new Button("Start");
        btn.setTranslateX(330);
        btn.setTranslateY(300);

        Label error1 = new Label("Please select an option");
        Label error2 = new Label("Please select an option");
        Label error3 = new Label("Please select depth");
        Label error4 = new Label("Please select depth");
        Label error5 = new Label("Please select condition");
        Label error6 = new Label("Please select condition");
        Label error7 = new Label("Depth must be a positive integer");
        Label error8 = new Label("Depth must be a positive integer");
        Label error9 = new Label("Condition must be a positive integer");
        Label error10 = new Label("Condition must be a positive integer");

        AtomicBoolean readyToStart = new AtomicBoolean(false);

        //pos int


        btn.setOnMouseClicked(e ->{
            //get labels' info
            readyToStart.set(true);

            pane.getChildren().remove(error1);
            pane.getChildren().remove(error2);
            pane.getChildren().remove(error3);
            pane.getChildren().remove(error4);
            pane.getChildren().remove(error5);
            pane.getChildren().remove(error6);
            pane.getChildren().remove(error7);
            pane.getChildren().remove(error8);
            pane.getChildren().remove(error9);
            pane.getChildren().remove(error10);

            if(choicesPlayer1.getValue() == null){
                readyToStart.set(false);
                error1.setTranslateX(choicesPlayer1.getTranslateX());
                error1.setTranslateY(choicesPlayer1.getTranslateY() + 25);
                error1.setTextFill(Color.RED);
                pane.getChildren().add(error1);
            }

            if(choicesPlayer2.getValue() == null){
                readyToStart.set(false);
                error2.setTranslateX(choicesPlayer2.getTranslateX());
                error2.setTranslateY(choicesPlayer2.getTranslateY() + 25);
                error2.setTextFill(Color.RED);
                pane.getChildren().add(error2);
            }

            if((choicesPlayer1.getValue()=="MINIMAX" || choicesPlayer1.getValue()=="MINIMAX with pruning") && depthValue1.getText().equals("")){
                readyToStart.set(false);
                error3.setTranslateX(depthValue1.getTranslateX());
                error3.setTranslateY(depthValue1.getTranslateY() + 25);
                error3.setTextFill(Color.RED);
                pane.getChildren().add(error3);
            }
            else if(( choicesPlayer1.getValue()=="MCTS" || choicesPlayer1.getValue()=="MCTS with rave") && depthValue1.getText().equals("") ){
                readyToStart.set(false);
                error5.setTranslateX(depthValue1.getTranslateX());
                error5.setTranslateY(depthValue1.getTranslateY() + 25);
                error5.setTextFill(Color.RED);
                pane.getChildren().add(error5);
            }

            if((choicesPlayer2.getValue()=="MINIMAX" || choicesPlayer2.getValue()=="MINIMAX with pruning") && depthValue2.getText().equals("")){
                readyToStart.set(false);
                error4.setTranslateX(depthValue2.getTranslateX());
                error4.setTranslateY(depthValue2.getTranslateY() + 25);
                error4.setTextFill(Color.RED);
                pane.getChildren().add(error4);
            }
            else if(( choicesPlayer2.getValue()=="MCTS" || choicesPlayer2.getValue()=="MCTS with rave") && depthValue2.getText().equals("")){
                readyToStart.set(false);
                error6.setTranslateX(depthValue2.getTranslateX());
                error6.setTranslateY(depthValue2.getTranslateY() + 25);
                error6.setTextFill(Color.RED);
                pane.getChildren().add(error6);
            }

            if((choicesPlayer1.getValue() == "MINIMAX" || choicesPlayer1.getValue() == "MINIMAX with pruning") && !isPositiveINT(depthValue1.getText(), 1)){
                readyToStart.set(false);
                error7.setTranslateX(depthValue1.getTranslateX());
                error7.setTranslateY(depthValue1.getTranslateY() + 25);
                error7.setTextFill(Color.RED);
                pane.getChildren().add(error7);
            }
            else if(( choicesPlayer1.getValue()=="MCTS" || choicesPlayer1.getValue()=="MCTS with rave") && !isPositiveINT(depthValue1.getText(), 0)){
                readyToStart.set(false);
                error9.setTranslateX(depthValue1.getTranslateX());
                error9.setTranslateY(depthValue1.getTranslateY() + 25);
                error9.setTextFill(Color.RED);
                pane.getChildren().add(error9);
            }

            if((choicesPlayer2.getValue()=="MINIMAX" || choicesPlayer2.getValue()=="MINIMAX with pruning") && !isPositiveINT(depthValue2.getText(), 1)){
                readyToStart.set(false);
                error8.setTranslateX(depthValue2.getTranslateX());
                error8.setTranslateY(depthValue2.getTranslateY() + 25);
                error8.setTextFill(Color.RED);
                pane.getChildren().add(error8);
            }
            else if(( choicesPlayer2.getValue()=="MCTS" || choicesPlayer2.getValue()=="MCTS with rave") && !isPositiveINT(depthValue2.getText(),0)){
                readyToStart.set(false);
                error10.setTranslateX(depthValue2.getTranslateX());
                error10.setTranslateY(depthValue2.getTranslateY() + 25);
                error10.setTextFill(Color.RED);
                pane.getChildren().add(error10);
            }

            if(readyToStart.get()){
                //set players
                if(choicesPlayer1.getValue() == "Player 1")
                    StartUI.playerOneAI = false;
                else if(choicesPlayer1.getValue()=="MCTS" || choicesPlayer1.getValue() == "MCTS with rave") {
                    StartUI.playerOneAI = true;
                    StartUI.AIone = 1;
                    StartUI.mcLIMIT = Integer.parseInt(depthValue1.getText());

                    StartUI.mctsWithRave = choicesPlayer1.getValue() == "MCTS with rave";
                }
                else if(choicesPlayer1.getValue()=="MINIMAX" || choicesPlayer1.getValue() == "MINIMAX with pruning"){
                    StartUI.playerOneAI = true;
                    StartUI.AIone = Integer.parseInt(depthValue1.getText()) + 1;
                }

                if(choicesPlayer2.getValue() == "Player 1")
                    StartUI.playerOneAI = false;
                else if(choicesPlayer2.getValue()=="MCTS" || choicesPlayer2.getValue() == "MCTS with rave") {
                    StartUI.playerTwoAI = true;
                    StartUI.AItwo = 1;
                    System.out.println("i am here");
                    StartUI.mcLIMIT = Integer.parseInt(depthValue2.getText());

                    StartUI.mctsWithRave = choicesPlayer1.getValue() == "MCTS with rave";
                }
                else if(choicesPlayer2.getValue()=="MINIMAX" || choicesPlayer2.getValue() == "MINIMAX with pruning"){
                    StartUI.playerTwoAI = true;
                    StartUI.AItwo = Integer.parseInt(depthValue2.getText()) + 1;
                }

                primaryStage.close();


                if(singleton == null) {
                	Main m = new Main();
                	singleton = m;
            }
                
                
                try{
                	singleton.start(singleton.stage);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });


        pane.getChildren().add(btn);


        pane.getChildren().add(depth2);

        Text title = new Text("CHECKERS");
        title.setFont(Font.font("Impact", FontWeight.BOLD, 50));
        title.setFill(Color.BLACK);
        title.setTranslateX(200);
        title.setTranslateY(50);

        pane.getChildren().add(title);

        Scene scene=new Scene(pane,700,400);
        primaryStage.setTitle("CHECKERS");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private boolean isPositiveINT(String str, int i){
        if(str == null)
            return false;
        int d;
        try {
            d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if(d < i)
            return false;
        return true;
    }
}
