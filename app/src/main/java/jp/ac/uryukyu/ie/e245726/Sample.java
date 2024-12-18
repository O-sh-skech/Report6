package jp.ac.uryukyu.ie.e245726;

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
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Title of the Window");

        button = new Button("click me");//ボタン
        button.setOnAction(e -> {
            boolean result =ConfirmBox.display("title of window", "are you sure sou want to send pic?");
            System.out.println(result);
        });

        StackPane layout = new StackPane();//レイアウト
        layout.getChildren().add(button);
        

        Scene scene = new Scene(layout,300,250);//シーン
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}