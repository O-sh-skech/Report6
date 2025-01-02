package jp.ac.uryukyu.ie.e245726;


import java.security.KeyStore.TrustedCertificateEntry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(1);
        grid.setHgap(10);

        //Home Lavel
        Label nameLabel = new Label("Username:");
        GridPane.setConstraints(nameLabel, 0,0);

        //Name inut
        TextField nameInput = new TextField("Bucky");
        GridPane.setConstraints(nameInput,1,0);//デフォ

        //Name inut
        Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel,0,1);

        //Password input
        TextField passInput = new TextField();
        passInput.setPromptText("password");//プロンプト
        GridPane.setConstraints(passInput, 1, 1);

        Button loginButton = new Button("log in");
        GridPane.setConstraints(loginButton, 1, 2);

        grid.getChildren().addAll(nameLabel,nameInput,passLabel,passInput,loginButton);

        Scene scene = new Scene(grid,300,200);
        


        window.setScene(scene);
        window.show();


    }
}