package jp.ac.uryukyu.ie.e245726;



import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class Sample extends Application {

    Stage primaryStage;
    Scene scene;
    Button button;

    @Override
    public void start(Stage primaryStage) {

        // 三角形メッシュを作成
        TriangleMesh meshFront = Mesh.createSurfaceMesh(10,1);
        TriangleMesh meshBack =  Mesh.createSurfaceMesh(10,0);
        

        // Y軸の設定（Cylinderで代用）
        Cylinder axisY = new Cylinder(0.2, 60); // 半径0.2、高さ60のシリンダー（Y軸）
        axisY.setMaterial(new javafx.scene.paint.PhongMaterial(Color.GREEN)); // 緑色に設定
        axisY.setRotationAxis(Rotate.Y_AXIS);
        axisY.setRotate(90); // 90度回転させる

        // メッシュをビューにラップ
        MeshView meshFrontView = new MeshView(meshFront);
        MeshView meshBackView = new MeshView(meshBack);

        // メッシュのマテリアル設定
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTBLUE); // 明るい青色
        material.setSpecularColor(Color.LIGHTGRAY); // 光沢
        meshFrontView.setMaterial(material);
        meshBackView.setMaterial(material);

        // カメラ設定
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-90);

        // シーン設定
        Group cameraGroup = new Group(camera);
        Group nodeGroup = new Group(meshFrontView, axisY);
        Group root = new Group(cameraGroup, nodeGroup);
        Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        scene.setCamera(camera);
        scene.setFill(Color.SKYBLUE);

        // ステージ設定
        
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