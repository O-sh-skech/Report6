package jp.ac.uryukyu.ie.e245726;

import javafx.scene.shape.TriangleMesh;
import java.util.ArrayList;

public class Mesh {
    public Mesh(){

    }
    /**
     * zPosのArrayListうちNaNのインデックスから新しいArrayListを作成する
     * @param size 円の半径
     * @param zArrayList zPosの座標のリスト
     * @return ArrayList<Integer> 
     */
    public static ArrayList<Integer[][]> NaNIndex(int size,ArrayList<Float> zArraylist){// zArraylistからNaNの時のインデックスを保存
        ArrayList<Integer[][]> NaNIndexlist = new ArrayList<>();
        for (int r = 0; r <= size; r++) {
            for (int θ = 0; θ <= 360; θ++) {
                if(Float.isNaN(zArraylist.get(r*361+θ))){
                    Integer[][] NaNTriangle = {{r,θ},{r,θ+1},{r+1,θ},{r+1,θ+1}};
                    NaNIndexlist.add(NaNTriangle);
                }
            }
        }return NaNIndexlist;
    }
    /**
     * TriangleMesh を作成する
     * @param size 円の半径
     * @param aspect 面の裏表
     * @return TriangleMesh
     */
    public static TriangleMesh createSurfaceMesh(int size, int aspect, String functionText) {//aspectは０（裏）か１（表）
        TriangleMesh mesh = new TriangleMesh();
        ArrayList<Float> zArraylist = new ArrayList<>();
        // 頂点の作成
        for (int r = 0; r <= size; r++) {
            for (int θ = 0; θ <= 360; θ++){
                float angle = (float) Math.toRadians(θ);
                float xPos = r * (float) Math.cos(angle);
                float yPos = r * (float) Math.sin(angle);
                float zPos = (float)FunctionSimpler.simpler(functionText, r, θ).evalf();// 高さを計算
                zArraylist.add(zPos);// NaNを"含む"リスト
                if(Float.isNaN(zPos)){// NaNを検出
                   zPos = 0;//(float)FunctionSimpler.simpler(functionText, r, θ+1).evalf();
                }
                if(zPos < 0){// NaNを含まないメッシュに利用される頂点群
                    mesh.getPoints().addAll(xPos, yPos, Math.max(zPos,-size*10^5));
                }else{
                    mesh.getPoints().addAll(xPos, yPos, Math.min(zPos,size*10^5));
                }

            }
        }

        // テクスチャ座標の作成
        for (int r = 0; r <= size; r++) {
            for (int θ = 0; θ <= 360; θ++){
           float u = θ/360f;
           float v = r/(float)(size+1);
           mesh.getTexCoords().addAll(u, v);
           }
        }

        // 三角形の作成
        for (int r = 0; r < size; r++) {
            for (int θ = 0; θ < 360; θ++) {
                int p00 = r * 361 + θ;
                int p01 = r * 361 + (θ + 1);
                int p10 = (r + 1) * 361 + θ;
                int p11 = (r + 1) * 361 + (θ + 1);

                int t00 = p00;
                int t01 = p01;
                int t10 = p10;
                int t11 = p11;

                

                for(Integer[][] index : Mesh.NaNIndex(size,zArraylist)){
                    if(index[0][0]==r && index[0][1]==θ){
                        t00 = 1;
                        t01 = 1;
                        t10 = 1;
                        t11 = 1;
                    }else{
                        t00 = p00;
                        t01 = p01;
                        t10 = p10;
                        t11 = p11;
                    }
                }
                if(aspect == 1){// 表面
                    // 上の三角形
                mesh.getFaces().addAll(p00, t00, p10, t10, p01, t01);

                    // 下の三角形
                mesh.getFaces().addAll(p10, t10, p11, t11, p01, t01);
                }else if(aspect == 0){// 裏面
                    // 上の三角形
                mesh.getFaces().addAll(p00, t00, p01, t01, p10, t10);

                    // 下の三角形
                mesh.getFaces().addAll(p10, t10, p01, t01, p11, t11);

                }
            }
        }
        return mesh;
    }
    
        public static void main(String[] args) {
        ArrayList<Float> zArraylist = new ArrayList<>();
        for (int r = 0; r <= 5; r++) {
            for (int θ = 0; θ <= 91; θ++) {
                //float zPos = (float)FunctionSimpler.simpler("x/y", r, θ).evalf();// 高さを計算
                //zArraylist.add(zPos);// NaNを"含む"リスト
                //System.out.println(zPos);
                
            }
        }
        for (int r = 0; r < 5; r++) {
            for (int θ = 0; θ < 5; θ++) {
                int p00 = r * 6 + θ;
                int p01 = r * 6 + (θ + 1);//あくまでも順番だから、シータが理屈に合っている必要ないのでは？p01=5*size+360,p10=6*size+0 
                int p10 = (r + 1) * 6 + θ;
                int p11 = (r + 1) * 6 + (θ + 1);
    
                //System.out.println(θ+" "+p00+" "+p01+" "+p10+" "+p11);
                }
            }
         // テクスチャ座標の作成
         for (int r = 0; r <= 5; r++) {
            for (int θ = 0; θ <= 5; θ++){
                   float u = θ/2.0f*(float)Math.PI;
                   float v = r/6.0f;
                   System.out.println("r = " + r + ", θ = " + θ + " => u = " + u + ", v = " + v);

                }
        }
        
        }
}
            
