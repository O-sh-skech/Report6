package jp.ac.uryukyu.ie.e245726;


import java.security.KeyStore.TrustedCertificateEntry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Sample extends Application{
    public static void main(String[] args) {
        launch(args);
    }

   
    Stage window;
    Scene scene;
    Button button;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Thenewboston");

        //form
        TextField nameInput = new TextField();

        button = new Button("ClickMe");
        button.setOnAction(e -> isInt(nameInput,nameInput.getText()));

        //Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(nameInput, button);

        Scene scene = new Scene(layout,300,200);
        window.setScene(scene);
        window.show();


    }
    private boolean isInt(TextField input, String message){
        try{
            int age = Integer.parseInt(input.getText());
            System.out.println("User is " + age);
            return true;
        }catch(NumberFormatException e){
            System.out.println("Error " + message + " is not a number");
            return false;
        }
    }
}