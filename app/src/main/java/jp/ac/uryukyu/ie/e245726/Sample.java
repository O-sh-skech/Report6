package jp.ac.uryukyu.ie.e245726;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Sample extends Application{

    Stage window;
    Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args);
    }


    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        Label label1 = new Label("welcom to first scene");
        Button button1 = new Button("go to scene2");
        button1.setOnAction(e -> window.setScene(scene2));

        //Layout 1 – children are laid out in vertical column
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1,button1);
        scene1 = new Scene(layout1, 200, 200);

        //Button
        Button button2 = new Button("This is scene sucks , go back to scene 1");
        button2.setOnAction(e -> window.setScene(scene1));

        //layout2
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(button2);
        scene2 = new Scene(layout2, 600, 900);

        window.setScene(scene1);
        window.setTitle("Title here");
        window.show();




    }
}
