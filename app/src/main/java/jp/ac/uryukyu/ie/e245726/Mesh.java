package jp.ac.uryukyu.ie.e245726;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Mesh{
    private TriangleMesh mesh;
    private MeshView meshView;

    Mesh(TriangleMesh mesh, MeshView meshView){
        this.mesh = mesh;
        this.meshView = meshView;
    }

    public TriangleMesh getMesh(){
        return mesh;
    }
    public MeshView getMeshView(){
        return meshView;
    }

}