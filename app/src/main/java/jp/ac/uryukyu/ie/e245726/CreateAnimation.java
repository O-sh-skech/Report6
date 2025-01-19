package jp.ac.uryukyu.ie.e245726;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.shape.Path;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.util.Duration;
import java.util.ArrayList;

/**
 * 関数グラフィックのアニメーションを生成します。
 * 極座標変換で描写しているのが分かります。
 */
public class CreateAnimation{

    Sphere sphere;
    Group traceBack;

    public CreateAnimation(Sphere sphere, Group traceBack){
        this.sphere = sphere;
        this.traceBack = traceBack;
    }
    public Sphere getSphere(){
        return sphere;
    }
    public Group getTraceBack(){
        return traceBack;
    }
    /**
     * アニメーションの軌跡を生成します。
     * @param point 2D動点
     * @param path　2D軌跡
     * @param angleXYZ 2D空間(XYかYZ)
     * @param otherWall 3D空間座標のリスト
     * @return PathTransition 3D空間座標における軌跡
     */
    private static PathTransition CreatePath(Circle point, Path path, String angleXYZ, ArrayList<float[]> otherWall){
        path.setStrokeWidth(1); //軌跡の太さ
        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(point); //アニメーション対象
        pathTransition.setPath(path); //軌跡としてのパス
        pathTransition.setDuration(Duration.seconds(40));
        if(angleXYZ == "XY"){
            path.getElements().add(new MoveTo(otherWall.get(0)[0],otherWall.get(0)[1]));
            for(float[] xyPos : otherWall){ //otherWallを引数にする
                path.getElements().add(new LineTo(xyPos[0],xyPos[1]));
        }}
        if(angleXYZ == "YZ"){
            path.getElements().add(new MoveTo(otherWall.get(0)[1],otherWall.get(0)[2]));
            for(float[] yzPos : otherWall){
                path.getElements().add(new LineTo(yzPos[1],yzPos[2]));
        }}
        pathTransition.play();
        return pathTransition;
    }
    /**
     * アニメーションを生成します。
     * @param otherWall 3D空間座標のリスト
     * @param currentGroup　アニメーショングループが保存されたグループ
     * @param eventGroup　関数グラフィックが保存されたグループ
     * @return Group アニメーションを保存するグループ
     */
    public static Group Animation(ArrayList<float[]> otherWall,Group currentGroup,Group eventGroup){
        Sphere sphere = new Sphere(1); //球を作成,半径1の球
        Group traceBack = new Group();
        Group traced = new Group();
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        sphere.setMaterial(material);
        for(float[] xyzPos : otherWall){
           if((xyzPos[5]==0.0 || xyzPos[5]==5.0 || xyzPos[5]==10.0) && xyzPos[6]==0 ){
                Cylinder cylinder = new Cylinder(xyzPos[5],0.1);
                cylinder.setMaterial(new PhongMaterial(Color.rgb(241, 13, 123,0.3)));
                traceBack.getChildren().addAll(cylinder);
            }
        }
        AnimationTimer timer = new AnimationTimer(){ //アニメーションタイマーで球を動かす
            Circle pointXY = new Circle();
            Path pathXY = new Path();
            PathTransition transitionXY = CreatePath(pointXY, pathXY, "XY",otherWall);
            Circle pointYZ = new Circle();
            Path pathYZ = new Path();
            PathTransition transitionYZ = CreatePath(pointYZ, pathYZ, "YZ",otherWall);
            @Override
            public void handle(long now) {   
                double currentX = pointXY.getTranslateX(); //現在の座標を取得
                double currentY = pointXY.getTranslateY();
                double currentZ = pointYZ.getTranslateY();    
                sphere.setTranslateX(currentX);
                sphere.setTranslateY(currentY);
                sphere.setTranslateZ(currentZ);
                Sphere trace = new Sphere(0.2);
                trace.setTranslateX(currentX);
                trace.setTranslateY(currentY);
                trace.setTranslateZ(currentZ);
                trace.setMaterial(material);
                traceBack.getChildren().addAll(trace);
                transitionXY.setOnFinished(event1 ->{
                    transitionYZ.setOnFinished(event2-> {
                        this.stop();
                        currentGroup.getChildren().clear();
                        currentGroup.getChildren().add(eventGroup);
                    });
                });   
            }
        };
        timer.start();
        traced.getChildren().addAll(sphere,traceBack);
        return traced;
    }
    
}
