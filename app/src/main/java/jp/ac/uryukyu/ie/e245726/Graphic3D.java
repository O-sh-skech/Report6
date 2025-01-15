package jp.ac.uryukyu.ie.e245726;


import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class Graphic3D extends Application {

    private Button button;
    private String functionText;
    private String[] palamate = {"x","y","+","-","×","÷","√","^","e"};
    private String[] inputPalamate = {"x","y","+","-","*","/","Power()","^","E"};

    //メッシュの初期化

    @Override
    public void start(Stage primaryStage) {
        // 三角形メッシュのプロパティ
        Group meshViewGroup = new Group();
            // メッシュのマテリアル設定
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(Graphic3D.class.getResourceAsStream("/resources/house.jpeg")));
        material.setDiffuseColor(Color.LIGHTBLUE); // 明るい青色
        material.setSpecularColor(Color.LIGHTGRAY); // 光沢

        //テキストフィールド
        TextField functionInput = new TextField();
        functionInput.setPromptText("f(x,y)=");
        button = new Button("ClickMe");
        button.setOnAction(e-> {
            functionText = functionInput.getText();
            // 入力が空かどうかを確認
            if (functionText == null || functionText.trim().isEmpty() ) {
                return;
            }
            meshViewGroup.getChildren().clear();
            System.out.println(functionText);
             
            // 三角形メッシュを作成
            for(int n = 0; n<=1; n ++){
                for(Mesh surface : CreateMesh.createSurfaceMesh(360,3,n, functionText)){
                    MeshView meshView = surface.getMeshView();
                    TriangleMesh mesh = surface.getMesh();
                    meshView.setMesh(mesh);
                    meshView.setMaterial(material);
                    meshViewGroup.getChildren().add(meshView);
                    System.out.println("Mesh Points Count: " + mesh.getPoints().size());
                    System.out.println("Mesh TexCoords Count: " + mesh.getTexCoords().size());
                    System.out.println("Mesh Faces Count: " + mesh.getFaces().size());

                }
            }
        });
            //テキストフィールドのレイアウト
        VBox layout = new VBox(10);
        HBox layoutUI = new HBox();//レイアウト
        layout.setPadding(new Insets(20,20,20,20));
        layoutUI.getChildren().add(button);
        //button.setTranslateX(5);
            //テキストフィールドの入力ボタン
        IntStream.range(0, palamate.length).forEach(i -> {
            Button buttonUI = new Button();
            buttonUI.setText(palamate[i]);
            buttonUI.setFocusTraversable(false);
            buttonUI.setOnAction(e -> {
            // プログラムでKeyEventを発生させる
                KeyEvent keyEvent = new KeyEvent(
                    KeyEvent.KEY_TYPED,    // イベントタイプ
                    inputPalamate[i],               // 文字（必要なら入力内容）
                    inputPalamate[i],                     // テキスト
                    KeyCode.UNDEFINED,          // キーコード（例: ENTER）
                    false, false, false, false // 修飾キー（Shift, Ctrl, Alt, Meta）
                );
                functionInput.fireEvent(keyEvent); // イベントをトリガー
            });
            buttonUI.setTranslateX(5*(i+1));//ボタンをずらす
            layoutUI.getChildren().add(buttonUI);//ボタン
        });
        layout.getChildren().addAll(functionInput, layoutUI);



        // X軸 (Z軸周りに90度回転)
        Cylinder axisX = new Cylinder(0.2, 60);
        axisX.setMaterial(new PhongMaterial(Color.BLUE));
        axisX.setRotationAxis(javafx.geometry.Point3D.ZERO.add(0, 0, 1)); // Y軸周りに回転
        axisX.setRotate(90); // 90度回転

        // Y軸 
        Cylinder axisZ = new Cylinder(0.2, 60); // 半径0.2, 高さ60
        axisZ.setMaterial(new PhongMaterial(Color.GREEN));

        // Z軸 (X軸周りに90度回転)
        Cylinder axisY = new Cylinder(0.2, 60);
        axisY.setMaterial(new PhongMaterial(Color.RED));
        axisY.setRotationAxis(javafx.geometry.Point3D.ZERO.add(1, 0, 0)); // X軸周りに回転
        axisY.setRotate(90); // 90度回転
     


        // カメラ設定
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-90);

        // シーン設定
            //グループ作成、物体とカメラを分ける
        Group cameraGroup = new Group(camera);
        Group nodeGroup = new Group(axisX,axisY,axisZ,meshViewGroup);
        Group root = new Group(cameraGroup, nodeGroup);
            //サブシーンの詳細設定
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