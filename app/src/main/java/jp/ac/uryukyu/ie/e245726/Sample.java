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
    Stage window;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("JavaFx = Thenewboston");

        window.setOnCloseRequest(e -> {
            e.consume();//リクエストを一時中断して、closeProgramを実行
            closeProgram();
        });//強制停止してもセーブ　大事

        button = new Button("close program");//ボタン
        button.setOnAction(e -> closeProgram());//close buttonを押してセーブ
        

        StackPane layout = new StackPane();//レイアウト
        layout.getChildren().add(button);
        

        Scene scene = new Scene(layout,300,250);//シーン
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void closeProgram(){
        Boolean answer = ConfirmBox.display("titile", "Sure you want to exit?");
        if(answer)//trueなら
        window.close();
    }
}