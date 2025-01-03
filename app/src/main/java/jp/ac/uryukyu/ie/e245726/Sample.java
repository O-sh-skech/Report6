package jp.ac.uryukyu.ie.e245726;


import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;

public class Sample extends Application {

    @Override
    public void start(Stage primaryStage) {


        // 三角形メッシュを作成
        TriangleMesh mesh = createSurfaceMesh(50, 50, 20);

        // Y軸の設定（Cylinderで代用）
        Cylinder axisY = new Cylinder(0.2, 60); // 半径0.2、高さ60のシリンダー（Y軸）
        axisY.setMaterial(new javafx.scene.paint.PhongMaterial(Color.GREEN)); // 緑色に設定
        axisY.setRotationAxis(Rotate.X_AXIS);
        axisY.setRotate(90);  // 90度回転させる


        // メッシュをビューにラップ
        MeshView meshView = new MeshView(mesh);
        meshView.setCullFace(CullFace.NONE); 

        // メッシュのマテリアル設定
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTBLUE); // 明るい青色
        material.setSpecularColor(Color.LIGHTGRAY); // 光沢
        meshView.setMaterial(material);

        // カメラ設定
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-50);

        // シーン設定
        Group cameraGroup = new Group(camera);
        Group nodeGroup = new Group(meshView, axisY);
        Group root =  new Group(cameraGroup, nodeGroup);
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

        //キー入力回転
            scene.setOnKeyPressed((KeyEvent event) -> {
        switch (event.getCode()) {
            case UP:
                rotateX.setAngle(rotateX.getAngle() - 10);
                break;
            case DOWN:
                rotateX.setAngle(rotateX.getAngle() + 10);
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

    /**
     * TriangleMesh を作成する
     * @param rows 縦の分割数
     * @param cols 横の分割数
     * @param size グリッドのサイズ
     * @return TriangleMesh
     */
    private TriangleMesh createSurfaceMesh(int rows, int cols, float size) {
        TriangleMesh mesh = new TriangleMesh();
        // 頂点の作成
        float stepX = size / cols;//一目盛り分の大きさ
        float stepY = size / rows;
        for (int y = 0; y <= rows; y++) {
            for (int x = 0; x <= cols; x++) {
                float xPos = x * stepX - size / 2;
                float yPos = y * stepY - size / 2;
                float zPos = (float)(xPos*0.1)/(float)(yPos*0.1);// 高さを計算
                
                
                mesh.getPoints().addAll(xPos, yPos, zPos);
            }
        }

        // テクスチャ座標の作成（ダミー）
        for (int y = 0; y <= rows; y++) {
            for (int x = 0; x <= cols; x++) {
                float u = (float) x / cols;
                float v = (float) y / rows;
                mesh.getTexCoords().addAll(u, v);
            }
        }

        // 三角形の作成
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int p00 = y * (cols + 1) + x;
                int p01 = y * (cols + 1) + x + 1;
                int p10 = (y + 1) * (cols + 1) + x;
                int p11 = (y + 1) * (cols + 1) + x + 1;

                // 上の三角形
                mesh.getFaces().addAll(p00, 0, p10, 0, p01, 0);

                // 下の三角形
                mesh.getFaces().addAll(p10, 0, p11, 0, p01, 0);
            }
        }

        return mesh;
    }

    public static void main(String[] args) {
        launch(args);
    }
}