package jp.ac.uryukyu.ie.e245726;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

public class CreateMesh {
    /**
     * 曲面の最小最大値 を定義する
     * @param size 円の半径
     * @param zPos f(x,y)の値
     * @return TriangleMesh
     */
    public static float MaxMinDivider(float zPos, int size){
        if(zPos < 0){
            zPos = Math.max(zPos,-size*10^5);
        }else{
            zPos = Math.min(zPos,size*10^5);
        }
        return zPos;
    }
    /**
     * ArrayListの各要素までの要素ArrayListの加算合計を求める
     * @param list ArrayList<ArrayList<T>>のリスト
     * @return ArrayList<Integer>
     */
    public static <T> ArrayList<Integer> listIndexSum(ArrayList<ArrayList<T>> list){
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
     * TriangleMesh を作成する
     * @param angle 角度
     * @param size 円の半径
     * @param aspect 面の裏表
     * @param functionText 任意の関数
     * @return TriangleMesh
     */
    public static ArrayList<Mesh> createSurfaceMesh(int angle, int size, int aspect, String functionText) {//aspectは０（裏）か１（表）
        
        int functionType = FunctionSimpler.simpler(functionText, 0, 0).getFunctionType();
        ArrayList<Mesh> meshGroup = new ArrayList<>();

        ArrayList<ArrayList<ArrayList<float[]>>> outerWall = new ArrayList<>();
        ArrayList<ArrayList<float[]>> innerWall = new ArrayList<>();
        ArrayList<float[]> wall = new ArrayList<>();

        ArrayList<ArrayList<float[]>> foundMeshList = new ArrayList<>();
        TriangleMesh foundMesh = new TriangleMesh();
        boolean isNaN = false;

        for (float r = 0; r <= size; r += 0.2) {
            innerWall.clear();
            for (int θ = 0; θ <= angle; θ++){
                System.out.println("wall.size()は "+ wall.size()+" functionTypeは " + functionType);
                
                float radian = (float) Math.toRadians(θ);
                float xPos = r * (float) Math.cos(radian);
                float yPos = r * (float) Math.sin(radian);
                float zPos = (float)FunctionSimpler.simpler(functionText, r, θ).getResult().evalf();// 高さを計算

                float u = r/(float)size;
                float v = θ/(float)angle;
                
                try{
                    // NaNを検出
                    if(Float.isNaN(zPos) && functionType != 1){
                        throw new IllegalArgumentException("zPos is NaN");
                    }else while(Float.isNaN(zPos)){
                        if(r == 0){//rの関数
                            zPos = MaxMinDivider((float)FunctionSimpler.simpler(functionText, r+1, θ).getResult().evalf(),size);
                        }else{
                            zPos = MaxMinDivider((float)FunctionSimpler.simpler(functionText, r-1, θ).getResult().evalf(),size);
                        }
                    }
                }catch(IllegalArgumentException e){
                    

                    if(functionType == 0 ){//θの関数
                        while(Float.isNaN(zPos)){
                        zPos = MaxMinDivider((float)FunctionSimpler.simpler(functionText, r, θ-1).getResult().evalf(),size);
                        }
                        }
                    if( functionType == 2){//θとrの関数
                        while(Float.isNaN(zPos)){
                        if(r == 0){
                            zPos = MaxMinDivider((float)FunctionSimpler.simpler(functionText, r+1, θ).getResult().evalf(),size);
                        }else{
                            zPos = MaxMinDivider((float)FunctionSimpler.simpler(functionText, r, θ-1).getResult().evalf(),size);
                        }
                        }
                    }

                    
                    isNaN = true;

                    System.out.println("NaNが検出されました。修正します。");
                    continue;//座標だけは保存される。
                }finally{
                    float[] xyzPos = {2*xPos, -2*zPos , 2*yPos, u, v};
                    wall.add(xyzPos);
                    if(isNaN == true || θ == angle){
                        innerWall.add(new CopyList(wall).getList());
                        System.out.println(wall.size()+"個の頂点が一括で保存されたし");
                        wall.clear();
                        isNaN = false;
                    }
                }    
            }
            outerWall.add(new CopyList(innerWall).getList());
        }
        int n = 0;
        System.out.println("innerWall.size()は "+ innerWall.size()+" outerWall.size()は " + outerWall.size());
        for(int inner = 0; inner < innerWall.size(); inner ++ ){
            foundMeshList.clear();
            for(int outer = 0; outer < outerWall.size(); outer ++ ){
                foundMeshList.add(outerWall.get(outer).get(inner));//a0,a1,a2,a3
            }
            for(int i = 0; i< foundMeshList.size(); i ++){//a0
                ArrayList<float[]> xyzPosList = foundMeshList.get(i);
                System.out.println(xyzPosList.get(0)[0] +" "+ xyzPosList.get(0)[1]+" "+ xyzPosList.get(0)[2]+" は頂点の座標なはず");
                System.out.println("a0のサイズは "+ xyzPosList.size()+" foundMeshListのサイズは "+ foundMeshList.size() + " outerWall.size()と同じはず");
                if(xyzPosList.size()<=1){
                    System.out.println("スキップされた");
                    continue;
                }
                for(float[] xyzPos : xyzPosList){//a0の頂点
                    foundMesh.getPoints().addAll(xyzPos[0],xyzPos[1],xyzPos[2]);
                    foundMesh.getTexCoords().addAll(xyzPos[3],xyzPos[4]);
                    System.out.println("頂点の数 "+n);
                    n+=1;
                }
                int wallSize = 0;//a0のサイズ
                if(i+1 < foundMeshList.size()){
                if(foundMeshList.get(i).size() <= foundMeshList.get(i+1).size()){
                    wallSize = foundMeshList.get(i).size();
                }else{
                    wallSize = foundMeshList.get(i+1).size();
                }}
                int lineIndex = 0;
                for(int listIndex : listIndexSum(foundMeshList)){//a0からanまでの加算
                    for(int PosIndex=0; PosIndex<wallSize-1; PosIndex++){
                    if(aspect == 0){
                        foundMesh.getFaces().addAll(PosIndex+lineIndex,PosIndex+lineIndex, PosIndex+listIndex,PosIndex+listIndex, PosIndex+lineIndex+1,PosIndex+lineIndex+1);//p$0, 0, p$0+xyzPosList$0.size(), 0, p$0+1, 0
                        foundMesh.getFaces().addAll(PosIndex+lineIndex+1,PosIndex+lineIndex+1, PosIndex+listIndex,PosIndex+listIndex, PosIndex+listIndex+1,PosIndex+listIndex+1);//p$0+1, 0, p$0+xyzPosList$0.size(), 0, p$0+1+xyzPosList$0.size(), 0
                    }
                    if(aspect == 1){
                        foundMesh.getFaces().addAll(PosIndex,PosIndex, PosIndex+1,PosIndex+1, PosIndex+listIndex,PosIndex+listIndex);//p$0, 0, p$0+xyzPosList$0.size(), 0, p$0+1, 0
                        foundMesh.getFaces().addAll(PosIndex+1,PosIndex+1, PosIndex+listIndex+1,PosIndex+listIndex+1, PosIndex+listIndex,PosIndex+listIndex);//p$0+1, 0, p$0+xyzPosList$0.size(), 0, p$0+1+xyzPosList$0.size(), 0
                    }
                    }
                    lineIndex += wallSize;
                }
                
            }
            TriangleMesh mesh = new TriangleMesh();
            MeshView meshView = new MeshView();
            mesh.getPoints().addAll(foundMesh.getPoints());
            mesh.getTexCoords().addAll(foundMesh.getTexCoords());
            mesh.getFaces().addAll(foundMesh.getFaces());
            meshGroup.add(new Mesh(mesh,meshView));
    
        }
        return meshGroup;
    }
}
