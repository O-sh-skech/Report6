package jp.ac.uryukyu.ie.e245726;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class Graphic3D extends Application {

    Stage primaryStage;
    Scene scene;
    SubScene subScene;
    Button button;
    String functionText;

    //メッシュの初期化
    TriangleMesh meshFront = new TriangleMesh();
    TriangleMesh meshBack = new TriangleMesh();
    MeshView meshFrontView = new MeshView(meshFront);
    MeshView meshBackView = new MeshView(meshBack);

    @Override
    public void start(Stage primaryStage) {

        //テキストフィールド
        TextField functionInput = new TextField();
        button = new Button("ClickMe");
        button.setOnAction(e-> {
            functionText = functionInput.getText();
            // 入力が空かどうかを確認
            if (functionText == null || functionText.trim().isEmpty()) {
                return;
            }
            // 三角形メッシュを作成
            meshFront = Mesh.createSurfaceMesh(10,1, functionText);
            meshBack =  Mesh.createSurfaceMesh(10,0, functionText);

            meshFrontView.setMesh(meshFront);
            meshBackView.setMesh(meshBack);

            
        });
            //テキストフィールドのレイアウト
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(functionInput, button);

        // Y軸の設定（Cylinderで代用）
        Cylinder axisY = new Cylinder(0.2, 60); // 半径0.2、高さ60のシリンダー（Y軸）
        axisY.setMaterial(new javafx.scene.paint.PhongMaterial(Color.GREEN)); // 緑色に設定
        axisY.setRotate(90);
        // Y軸の設定（Cylinderで代用）
        Cylinder axisZ = new Cylinder(0.2, 60); // 半径0.2、高さ60のシリンダー（Y軸）
        axisZ.setMaterial(new javafx.scene.paint.PhongMaterial(Color.RED)); // 緑色に設定

        // 三角形メッシュのプロパティ
            // メッシュのマテリアル設定
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(Graphic3D.class.getResourceAsStream("/resources/house.jpeg")));
        //material.setDiffuseColor(Color.LIGHTBLUE); // 明るい青色
        material.setSpecularColor(Color.LIGHTGRAY); // 光沢
        meshFrontView.setMaterial(material);
        meshBackView.setMaterial(material);

        // カメラ設定
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-90);

        // シーン設定
            //グループ作成、物体とカメラを分ける
        Group cameraGroup = new Group(camera);
        Group nodeGroup = new Group(meshFrontView, meshBackView, axisY,axisZ);
        Group root = new Group(cameraGroup, nodeGroup);
            //シーンの詳細設定
        SubScene subScene = new SubScene(root, 700, 400, true,SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.SKYBLUE);
            //サブシーンがクリックされたときにフォーカスを設定
        subScene.setOnMouseClicked(event -> {
            subScene.requestFocus();
            layout.setMouseTransparent(false);
        });
        layout.setOnMouseClicked(event ->{
            layout.requestFocus();
            subScene.setMouseTransparent(false);
        });
            //配置を決める
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(subScene,layout);
            // ステージ設定
        Scene scene = new Scene(stackPane, 800 , 600);
        primaryStage.setTitle("Simple TriangleMesh Example");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 回転
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        cameraGroup.getTransforms().addAll(rotateX, rotateY);
            // キー入力回転
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case UP:
                    rotateX.setAngle(rotateX.getAngle() + 10);
                    break;
                case DOWN:
                    rotateX.setAngle(rotateX.getAngle() - 10);
                    break;
                case LEFT:
                    rotateY.setAngle(rotateY.getAngle() - 10);
                    break;
                case RIGHT:
                    rotateY.setAngle(rotateY.getAngle() + 10);
                    break;
                default:
                    break;
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}