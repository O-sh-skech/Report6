package jp.ac.uryukyu.ie.e245726;

import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Sample extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    Button button;
    Button button2;
    String[] palamate = {"x","y","z"};

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Title of the Window");

        StackPane layout = new StackPane();//レイアウト

        IntStream.range(0, palamate.length).forEach(i -> {
            Button button = new Button();
            button.setText(palamate[i]);
            button.setOnAction(e -> System.out.println(palamate[i]));
            button.setTranslateX(50*i);//ボタンをずらす
            layout.getChildren().add(button);//ボタン
        });

        Scene scene = new Scene(layout,300,250);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}