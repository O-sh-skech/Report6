package jp.ac.uryukyu.ie.e245726;

import java.util.stream.IntStream;
import org.fxyz3d.shapes.primitives.Text3DMesh;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * ウィンドウのレイアウトを各機能ごとにメソッド単位で管理します。
 */
public class Graphic3D extends Application {

    private Button button;
    private String functionText;
    private String[] palamate = {"x","y","+","-","×","÷","√","^","e"};
    private String[] inputPalamate = {"x","y","+","-","*","/","Power()","^","E"};
    /**
     * ウィンドウのレイアウトを生成します。
     * @param surfaceViewGroup 生成された関数グラフィック
     * @param material 関数グラフィックに適応されるテクスチャ
     * @return VBox VBoxレイアウトを使用しています。
     */
    private VBox makingLayout(Group surfaceViewGroup, PhongMaterial material){
        //テキストフィールド
        TextField functionInput = new TextField();
        Button buttonPlay = new Button("PLAY ANIMATION");
        buttonPlay.setStyle("-fx-background-color:rgb(226, 143, 9);"); 
        functionInput.setPromptText("f(x,y)=");
        functionInput.setStyle("-fx-background-color:rgb(241, 216, 231);"); 
        button = new Button("ClickMe");
        button.setStyle("-fx-background-color:rgb(226, 143, 9);"); 
        button.setOnAction(e-> {
            functionText = functionInput.getText();
            if (functionText == null || functionText.trim().isEmpty() ){ //入力が空かどうかを確認
                return;
            }
            makingMeshGroup(buttonPlay, surfaceViewGroup, material,360,10);
              });
        //テキストフィールドのレイアウト
        VBox layout = new VBox(10);
        HBox layoutUI = new HBox(); 
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); 
        spacer.setMouseTransparent(true);
        layout.setPadding(new Insets(20,20,20,20));
        layoutUI.getChildren().addAll(button);
            //テキストフィールドの入力ボタン
        IntStream.range(0, palamate.length).forEach(i -> {
            Button buttonUI = new Button();
            buttonUI.setText(palamate[i]);
            buttonUI.setFocusTraversable(false);
            buttonUI.setStyle("-fx-background-color:rgb(220, 141, 7);"); 
            buttonUI.setOnAction(e -> {
                KeyEvent keyEvent = new KeyEvent( //プログラムでKeyEventを発生させる
                    KeyEvent.KEY_TYPED,    
                    inputPalamate[i],               
                    inputPalamate[i],                     
                    KeyCode.UNDEFINED,          
                    false, false, false, false 
                );
                functionInput.fireEvent(keyEvent); 
            });
            buttonUI.setTranslateX(5*(i+1));
            layoutUI.getChildren().add(buttonUI);
        });
        layoutUI.getChildren().addAll(spacer, buttonPlay);
        layout.getChildren().addAll(functionInput, layoutUI);
        return layout;
    }
    /**
     * 生成された関数グラフィックを可視化します
     * @param buttonPlay アニメーション再生ボタン
     *  @param surfaceViewGroup 生成された関数グラフィック
     * @param material 関数グラフィックに適応されるテクスチャ
     * @param angle 角度
     * @param size 円の半径
     */
    private void makingMeshGroup(Button buttonPlay, Group surfaceViewGroup, PhongMaterial material,int angle,int size){
        // 三角形メッシュのプロパティ
        Group meshViewGroup = new Group();
        surfaceViewGroup.getChildren().clear();
        MeshCreated meshCreated = CreateMesh.createSurfaceMesh(angle, size, functionText);
        for(Mesh surface : meshCreated.getMeshGroup()){
            MeshView meshView = surface.getMeshView();
            TriangleMesh mesh = surface.getMesh();
            meshView.setMesh(mesh);
            meshView.setMaterial(material);
            meshViewGroup.getChildren().add(meshView);
            }
        surfaceViewGroup.getChildren().add(meshViewGroup);
        buttonPlay.setOnAction(animation ->{
            surfaceViewGroup.getChildren().clear();
            surfaceViewGroup.getChildren().add(CreateAnimation.Animation(meshCreated.getOtherWall(),surfaceViewGroup,meshViewGroup));
        });
    }
    /**
     * 基底を生成します。
     * @param XYZ 基底軸(XかYかZ)
     * @return Cylinder 円柱で基底軸が表現されます。
     */
    private Group makingAxis(String XYZ){
        Group axisGroup = new Group();
        Cylinder axis = new Cylinder(0.2, 60);
        if(XYZ == "X"){
        //X軸
        Text3DMesh text3D = new Text3DMesh("x","",5,false,0.1,0,0);
        text3D.setTranslateX(-30);
        text3D.setTranslateY(0);
        axis.setMaterial(new PhongMaterial(Color.rgb(8, 39, 88)));
        axis.setRotationAxis(javafx.geometry.Point3D.ZERO.add(0, 0, 1)); 
        axis.setRotate(90); 
        axisGroup.getChildren().addAll(axis,text3D);
        }
        if(XYZ == "Y"){
        //Y軸 
        axis.setMaterial(new PhongMaterial(Color.rgb(241, 13, 123)));
        axisGroup.getChildren().addAll(axis);
        }
        if(XYZ == "Z"){
        //Z軸
        Text3DMesh text3D = new Text3DMesh("y","Arial",6,false,0.1,0,0);
        text3D.setTranslateZ(30);
        text3D.setTranslateY(0);
        text3D.setRotationAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));
        text3D.setRotate(90);
        axis.setMaterial(new PhongMaterial(Color.rgb(8, 37, 80)));
        axis.setRotationAxis(javafx.geometry.Point3D.ZERO.add(1, 0, 0)); 
        axis.setRotate(90); 
        axisGroup.getChildren().addAll(axis,text3D);
        }
        return axisGroup;
    }
    /**
     * 回転UIを生成します。
     * @param cameraGroup カメラ系
     * @param scene ウィンドウ
     * @param rotateX 相対軸x
     * @param rotateY 相対軸y
     */
    private void makingUI(Group cameraGroup,Scene scene,Rotate rotateX,Rotate rotateY){
        
        cameraGroup.getTransforms().addAll(rotateX, rotateY);
            // キー入力回転
        scene.setOnKeyPressed((KeyEvent event) -> {
            int n = adjustAxis(rotateX, rotateY);
            switch (event.getCode()) {
                case UP:
                    rotateX.setAngle((rotateX.getAngle() + n*10)%360);
                    break;
                case DOWN:
                    rotateX.setAngle((rotateX.getAngle() - n*10)%360);
                    break;
                case LEFT:
                    rotateY.setAngle((rotateY.getAngle() - 10)%360);
                    break;
                case RIGHT:
                    rotateY.setAngle((rotateY.getAngle() + 10)%360);
                    break;
                default:
                    break;
            }
        });
        scene.setOnMousePressed((MouseEvent pressMe) -> {
            scene.setOnMouseDragged((MouseEvent draggMe) -> {
                int n = adjustAxis(rotateX, rotateY);
                rotateX.setAngle(rotateX.getAngle() - n*(draggMe.getSceneY()-pressMe.getSceneY())/50);    
                rotateY.setAngle(rotateY.getAngle() + (draggMe.getSceneX()-pressMe.getSceneX())/50); 
            });
        });
    }
    /**
     * makingUIのヘルパーメソッドです。
     * @return int 3D空間内での移動に対して回転が相対的に行われるように補正します。
     */
    private int adjustAxis(Rotate rotateX,Rotate rotateY){
        int n=1;
            if(rotateY.getAngle()>=0 && rotateY.getAngle()<90 || rotateY.getAngle()>=270 && rotateY.getAngle()<360)
                n = 1;
            else if (rotateY.getAngle()>=90 && rotateY.getAngle()<270)
                n = -1;
        return n;
    }
    /**
     * ウィンドウを生成します。
     */
    @Override
    public void start(Stage primaryStage) {
        
        //三角形メッシュのプロパティ
        Group surfaceViewGroup = new Group();
            //メッシュのマテリアル設定
        PhongMaterial material = new PhongMaterial();
        //material.setDiffuseMap(new Image(Graphic3D.class.getResourceAsStream("/resources/house.jpeg"))); //画像を選択できます。現在resourcesには""house"の画像が用意されています。
        material.setDiffuseColor(Color.rgb(232, 217, 220)); 
        material.setSpecularColor(Color.rgb(199, 58, 91)); 
        VBox layout = makingLayout(surfaceViewGroup, material);
        //カメラ設定
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-90);
        //シーン設定
            //グループ作成、物体とカメラを分ける
        Group cameraGroup = new Group(camera);
        Group nodeGroup = new Group(makingAxis("X"),makingAxis("Y"),makingAxis("Z"),surfaceViewGroup);
        Group root = new Group(cameraGroup, nodeGroup);
            //サブシーンの詳細設定
        SubScene subScene = new SubScene(root, 700, 400, true,SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.rgb(232, 138, 16));
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
        stackPane.setStyle("-fx-background-color:rgb(27, 44, 51);"); 
            //ステージ設定
        Scene scene = new Scene(stackPane, 800 , 600);
        scene.setFill(Color.rgb(6, 40, 72));
        primaryStage.setTitle("SURFACE GRAPHIC 3D");
        primaryStage.setScene(scene);
        primaryStage.show();
        //回転
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        makingUI(cameraGroup, scene, rotateX, rotateY);
    }

}