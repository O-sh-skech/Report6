package jp.ac.uryukyu.ie.e245726;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import java.util.ArrayList;

public class CreateMesh {
    /**
     * 
     * @deprecated このメソッドは廃止されたか未使用です
     * zPosのArrayListうちNaNのインデックスから新しいArrayListを作成する
     * @param size 円の半径
     * @param zArrayList zPosの座標のリスト
     * @return ArrayList<Integer> 
     */
    @Deprecated
    public static ArrayList<Integer[][]> NaNIndex(int angle, int size,ArrayList<Float> zArraylist){// zArraylistからNaNの時のインデックスを保存
        ArrayList<Integer[][]> NaNIndexlist = new ArrayList<>();
        for (int r = 0; r <= size; r++)  {
            for (int θ = 0; θ <= angle; θ++)  {
                if(Float.isNaN(zArraylist.get(r*(angle+1)+θ))){
                    Integer[][] NaNTriangle = {{r,θ},{r,θ+1},{r+1,θ},{r+1,θ+1}};
                    NaNIndexlist.add(NaNTriangle);
                } 
            }
        }return NaNIndexlist;
    }
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
     * TriangleMesh を作成する
     * @param angle 角度
     * @param size 円の半径
     * @param aspect 面の裏表
     * @param functionText 任意の関数
     * @return TriangleMesh
     */
    public static ArrayList<Mesh> createSurfaceMesh(int angle, int size, int aspect, String functionText) {//aspectは０（裏）か１（表）
        int n = 0;//ループ回数
        int p =0;//頂点の数。実験
        int NaNPoint = 0;
        int carrentPoint = 0;
        boolean isNaN = false;//NaNか否か
        ArrayList<Mesh> meshGroup = new ArrayList<>();
        TriangleMesh foundMesh = new TriangleMesh();
        for (int θ = 0; θ <= angle; θ++) {//θで分断される
            for (int r = 0; r <= size; r++){
                System.out.println(n+"回目です" +" θ= " + θ +" r= " + r);

                
                // 頂点の作成
                float radian = (float) Math.toRadians(θ);
                float xPos = r * (float) Math.cos(radian);
                float yPos = r * (float) Math.sin(radian);
                float zPos = (float)FunctionSimpler.simpler(functionText, r, θ).evalf();// 高さを計算
                
                // テクスチャ座標の作成
                float u = r/(float)size;
                float v = θ/(float)angle;
                
                

                // NaNを検出
                try{
                    if(Float.isNaN(zPos)){
                        throw new IllegalArgumentException("zPos is NaN");
                    }
                }catch(IllegalArgumentException e){
                    zPos = MaxMinDivider((float)FunctionSimpler.simpler(functionText, r, θ-1).evalf(),size);
                    System.out.println("NaNが検出されました");
                    NaNPoint = θ*(size+1)+r;
                    isNaN = true;
                    continue;//座標だけは保存される。
                }finally{

                foundMesh.getPoints().addAll(xPos,yPos,zPos);
                foundMesh.getTexCoords().addAll(u,v);
               
                System.out.println("x= "+xPos+" y= "+yPos+" z= "+zPos+ "頂点のインデックス= " + p);
                System.out.println(" u= " +u+" v= "+v);
                p += 1;
                //メッシュを新たに作るか否か
                if(isNaN == true || r == size && θ == angle){
                    if(n==0 && r == size ||n != 0 &&  isNaN==false){
                    
                    TriangleMesh mesh = new TriangleMesh();
                    MeshView meshView = new MeshView();
                        mesh.getPoints().addAll(foundMesh.getPoints());
                        mesh.getTexCoords().addAll(foundMesh.getTexCoords());
                        mesh.getFaces().addAll(foundMesh.getFaces());
                    meshGroup.add(new Mesh(mesh,meshView));
                    System.out.println("新しいメッシュが作成されました"+n);
                    }
                    if(carrentPoint>NaNPoint){
                    System.out.println("クリアしました");
                    foundMesh.getFaces().clear();
                    }
                    n = 0;
                    isNaN = false;
                }
                carrentPoint = θ*(size+1)+r;

                    
                }
                
                if(r != size && θ != angle){
                // 三角形の作成
                int p00 = θ*(size+1)+r;
                int p01 = θ*(size +1)+r+1;
                int p10 = (θ+1)*(size +1)+r;
                int p11 = (θ+1)*(size +1)+r+1;

                if(aspect == 1){// 表面
                foundMesh.getFaces().addAll(p00, p00, p01, p01, p10, p10);
                foundMesh.getFaces().addAll(p10, p10, p01, p01, p11, p11);
                System.out.println("インデクス " + p00+ " "+ p00+" "+p01+" "+ p01+" "+ p10+" "+ p10);
                System.out.println(p10+" "+ p10+" "+ p01+" "+ p01+" "+p11+" "+ p11);
                }else if(aspect == 0){// 裏面
                foundMesh.getFaces().addAll(p00, p00, p10, p10, p01, p01);
                foundMesh.getFaces().addAll(p10, p10, p11, p11, p01, p01);
                System.out.println(p00+" "+ p00+" "+ p10+" "+ p10+" "+ p01+" "+ p01);
                    System.out.println(p10+" "+ p10+" "+ p11+" "+ p11+" "+ p01+" "+ p01);
                }
                }
                
                

                n += 1;
                
                
            
            }
        
    
        }
        return meshGroup;
    }
}
