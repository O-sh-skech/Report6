package jp.ac.uryukyu.ie.e245726;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import java.util.ArrayList;

/**
 * 関数グラフィック生成の計算、統合を行います。
 * 極座標変換された座標系で計算することで、単純化を図っています。
 */
public class CreateMesh {

    /**
     * 関数グラフィックの最小値、最大値を定義します。
     * @param size 円の半径
     * @param zPos f(x,y)の値
     * @return float 補正されたf(x,y)の値
     */
    private static float MaxMinDivider(float zPos, int size){
        if(zPos < 0){
            zPos = Math.max(zPos,-size*10^5);
        }else{
            zPos = Math.min(zPos,size*10^5);
        }
        return zPos;
    }
    /**
     * ArrayListの各要素までの要素ArrayListの加算合計を求めます。
     * @param list ArrayList<ArrayList<T>>のリスト
     * @return ArrayList<Integer> 加算合計のリスト
     */
    private static <T> ArrayList<Integer> listIndexSum(ArrayList<ArrayList<T>> list){
        int sum = 0;
        ArrayList<Integer> sumList = new ArrayList<>();
        for (int i = 0; i < list.size()-1; i++) {
            if(list.get(i).size()<=1){
                continue;
            }
            sum += list.get(i).size(); 
            sumList.add(sum);
        }
        return sumList;
    }
    /**
     * 関数グラフィックにおけるNaNのfunctionTypeに基づき値を修正します。
     * @param r,θ　現在の半径と角度の値
     * @param functionText 任意の関数
     * @param size 円の半径
     * @param adjustRθ trueならRがfalseならθがfunctionTypeに基づき修正されます
     * @return float 補正されたf(x,y)の値
     */
    private static float adjustZPos(float r, int θ, String functionText, int size, boolean adjustRθ) {
        float newR = adjustRθ ? r + 0.1f : r;
        int newTheta = adjustRθ ? θ : θ - 1;
        return MaxMinDivider((float) FunctionSimpler.simpler(functionText, newR, newTheta).getResult().evalf(), size);
    }
    /**
     * TriangleMeshに必要なデータを生成します。
     * @param angle 角度
     * @param size 円の半径
     * @param functionText 任意の関数
     * @return MeshCalculated メッシュやアニメーション生成に必要な3D空間座標を格納するリスト群
     */
    private static MeshCalculated calculateSurfaceMesh(int angle, int size, String functionText) {
        int functionType = FunctionSimpler.simpler(functionText, 0, 0).getFunctionType();
        ArrayList<ArrayList<ArrayList<float[]>>> outerWall = new ArrayList<>();
        ArrayList<ArrayList<float[]>> innerWall = new ArrayList<>();
        ArrayList<float[]> wall = new ArrayList<>();
        ArrayList<float[]> otherWall = new ArrayList<>();
        boolean isNaN = false;
        for (float r = 0; r <= size; r ++) {
            innerWall.clear();
            for (int θ = 0; θ <= angle; θ++){
                float radian = (float) Math.toRadians(θ);
                float xPos = r * (float) Math.cos(radian);
                float yPos = r * (float) Math.sin(radian);
                float zPos = (float)FunctionSimpler.simpler(functionText, r, θ).getResult().evalf();// 高さを計算
                float u = r/(float)size;
                float v = θ/(float)angle;          
                try {
                    if (Float.isNaN(zPos)) {// NaNを検出
                        if (functionType != 1 && (functionType != 2 || r != 0)){ 
                            throw new IllegalArgumentException("zPos is NaN");
                        }else while (Float.isNaN(zPos)) {// functionType == 1 の場合はループ処理
                            zPos = adjustZPos(r, θ, functionText, size, true);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    while (Float.isNaN(zPos)) {
                        zPos = adjustZPos(r, θ, functionText, size, false);
                    }isNaN = true;
                    continue;//座標だけは保存される。
                }finally{
                    float[] xyzPos = {xPos, -zPos , yPos, u, v, r, θ};
                    if((r==0 || r==5 || r== 10) && θ<=angle)
                        otherWall.add(xyzPos);
                    wall.add(xyzPos);
                    if(isNaN == true || θ == angle){
                        innerWall.add(new CopyList(wall).getList());
                        wall.clear();
                        isNaN = false;
                    }
                }    
            }outerWall.add(new CopyList(innerWall).getList());
        }return new MeshCalculated(outerWall,innerWall,otherWall);
    }
    /**
     * TriangleMesh を作成する
     * @param angle 角度
     * @param size 円の半径
     * @param functionText 任意の関数
     * @return MeshCreated　3D空間座標の並び方を三角形ごと、時計回りに格納したNode型とアニメーション生成に要求される3D空間座標
     */
    public static MeshCreated createSurfaceMesh(int angle, int size, String functionText){
        ArrayList<Mesh> meshGroup = new ArrayList<>();
        ArrayList<ArrayList<float[]>> foundMeshList = new ArrayList<>();
        TriangleMesh foundMesh = new TriangleMesh();
        MeshCalculated materialMesh = calculateSurfaceMesh(angle, size, functionText);
        ArrayList<ArrayList<ArrayList<float[]>>>  outerWall = materialMesh.getOuterWall();
        ArrayList<ArrayList<float[]>> innerWall = materialMesh.getInnerWall();
        ArrayList<float[]>  otherWall = materialMesh.getOtherWall();
        for(int inner = 0; inner < innerWall.size(); inner ++ ){
            foundMeshList.clear();
            for(int outer = 0; outer < outerWall.size(); outer ++ ){
                foundMeshList.add(outerWall.get(outer).get(inner)); 
            }
            for(int i = 0; i< foundMeshList.size(); i ++){ 
                ArrayList<float[]> xyzPosList = foundMeshList.get(i);
                if(xyzPosList.size()<=1){
                    continue;
                }
                for(float[] xyzPos : xyzPosList){
                    foundMesh.getPoints().addAll(xyzPos[0],xyzPos[1],xyzPos[2]);
                    foundMesh.getTexCoords().addAll(xyzPos[3],xyzPos[4]);
                }
                int wallSize = 0;
                if(i+1 < foundMeshList.size()){
                if(foundMeshList.get(i).size() <= foundMeshList.get(i+1).size()){
                    wallSize = foundMeshList.get(i).size();
                }else{
                    wallSize = foundMeshList.get(i+1).size();
                }}
                int lineIndex = 0;
                for(int listIndex : listIndexSum(foundMeshList)){
                    for(int PosIndex=0; PosIndex<wallSize-1; PosIndex++){
                        foundMesh.getFaces().addAll(PosIndex+lineIndex,PosIndex+lineIndex, PosIndex+listIndex,PosIndex+listIndex, PosIndex+lineIndex+1,PosIndex+lineIndex+1);
                        foundMesh.getFaces().addAll(PosIndex+lineIndex+1,PosIndex+lineIndex+1, PosIndex+listIndex,PosIndex+listIndex, PosIndex+listIndex+1,PosIndex+listIndex+1);
                        foundMesh.getFaces().addAll(PosIndex+lineIndex,PosIndex+lineIndex, PosIndex+lineIndex+1,PosIndex+lineIndex+1, PosIndex+listIndex,PosIndex+listIndex);
                        foundMesh.getFaces().addAll(PosIndex+lineIndex+1,PosIndex+lineIndex+1, PosIndex+listIndex+1,PosIndex+listIndex+1, PosIndex+listIndex,PosIndex+listIndex);
                    }lineIndex += wallSize;
                }
            }
            TriangleMesh mesh = new TriangleMesh();
            MeshView meshView = new MeshView();
            mesh.getPoints().addAll(foundMesh.getPoints());
            mesh.getTexCoords().addAll(foundMesh.getTexCoords());
            mesh.getFaces().addAll(foundMesh.getFaces());
            meshGroup.add(new Mesh(mesh,meshView));
            foundMesh.getFaces().clear();
            foundMesh.getTexCoords().clear();
            foundMesh.getPoints().clear();
        }return new MeshCreated(meshGroup, otherWall);
    }

}

/**
 * createSurfaceMeshの戻り値に必要とされます。
 */
class MeshCreated{

    private ArrayList<Mesh> meshGroup;
    private ArrayList<float[]> otherWall;

    public MeshCreated(ArrayList<Mesh> meshGroup, ArrayList<float[]>  otherWall){
        this.meshGroup = meshGroup;
        this.otherWall = otherWall;
    }
    public ArrayList<Mesh> getMeshGroup(){
        return meshGroup;
    }
    public ArrayList<float[]> getOtherWall(){
        return otherWall;
    }

}

/**
 * calculateSurfaceMeshの戻り値に必要とされます。
 */
class MeshCalculated{ 

    private ArrayList<ArrayList<ArrayList<float[]>>> outerWall;
    private ArrayList<ArrayList<float[]>> innerWall;
    private ArrayList<float[]> otherWall;

    public MeshCalculated(ArrayList<ArrayList<ArrayList<float[]>>> outerWall,ArrayList<ArrayList<float[]>> innerWall,  ArrayList<float[]> otherWall){
        this.outerWall = outerWall;
        this.innerWall = innerWall;
        this.otherWall = otherWall;
    }
    public ArrayList<ArrayList<ArrayList<float[]>>> getOuterWall(){
        return outerWall;
    }
    public ArrayList<ArrayList<float[]>> getInnerWall(){
        return innerWall;
    }
    public ArrayList<float[]> getOtherWall(){
        return otherWall;
    }

}

/**
 * NaNで切り離されたメッシュ単位で格納されます。
 */
class Mesh{ 

    private TriangleMesh mesh;
    private MeshView meshView;
    private ArrayList<float[]> otherWall;

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
    public ArrayList<float[]> getOtherWall(){
        return otherWall;
    }

}

/**
 * リストのコピーを行います。
 */
class CopyList<T> { 

    private ArrayList<T> list = new ArrayList<>();

    CopyList(ArrayList<T> list) {
        this.list = new ArrayList<>(list); 
    }
    public ArrayList<T> getList() { 
        return list;
    }

}
