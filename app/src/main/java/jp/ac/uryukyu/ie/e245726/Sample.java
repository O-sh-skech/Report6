package jp.ac.uryukyu.ie.e245726;

import javax.swing.BorderFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Sample extends Application{
    public static void main(String[] args) {
        launch(args);
    }

   
    Stage window;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("JavaFx = Thenewboston");

        HBox topmenue = new HBox();
        Button buttonA = new Button("File");
        Button buttonB = new Button("Edit");
        Button buttonC = new Button("View");
        topmenue.getChildren().addAll(buttonA,buttonB,buttonC);

        VBox leftmenue = new VBox();
        Button buttonD = new Button("D");
        Button buttonE = new Button("E");
        Button buttonF = new Button("F");
        leftmenue.getChildren().addAll(buttonD,buttonE,buttonF);

        BorderPane borderpane = new BorderPane();
        borderpane.setTop(topmenue);
        borderpane.setLeft(leftmenue);

        Scene scene = new Scene(borderpane,300,250);//シーン
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void closeProgram(){
        Boolean answer = ConfirmBox.display("titile", "Sure you want to exit?");
        if(answer)//trueなら
        window.close();
    }
}