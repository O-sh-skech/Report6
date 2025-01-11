package jp.ac.uryukyu.ie.e245726;

import java.util.ArrayList;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class FunctionSimpler {
    public static IExpr simpler(String function, int r, int θ) {
        // ExprEvaluatorを作成
        ExprEvaluator evaluator = new ExprEvaluator();
        
        // 変数θをSymjaに設定
        evaluator.eval("angle = " + θ + " Degree");
        
        // r*cos(θ)とr*sin(θ)を設定
        evaluator.eval("x = r * cos(angle)");
        evaluator.eval("y = r * sin(angle)");

        // function（zPosの式）を設定
        evaluator.eval("z = " + function);

        // 変数rをSymjaに設定
        evaluator.eval("r ="+ r);

        // 結果を取得
        IExpr result = evaluator.eval("z");
        if (result.toString().equals("ComplexInfinity")) {
            //IExpr result1 = FunctionSimpler.simpler("x/y", 1, angle+(float)Math.PI/180);
            //return result1;
            return F.num(Double.NaN);
            //return evaluator.getVariable("NaN");
        }else{
            return result;
        }
    }
    public static void main(String[] args) {

        for(Mesh surface : CreateMesh.createSurfaceMesh(5,2,1, "x/y")){
            MeshView meshView = surface.getMeshView();
            TriangleMesh mesh = surface.getMesh();
            meshView.setMesh(mesh);
            
            System.out.println(mesh);
        }
        


        
        int size = 2;
        int angle =5;
        
        
        

        int n = 0;//ループ回数
        boolean isNaN = false;//NaNか否か
        int aspect = 1;
        for (int θ = 0; θ <= angle; θ++) {//θで分断される
            for (int r = 0; r <= size; r++){
                System.out.println(n+"回目です");

                //メッシュを新たに作るか否か
                if( n==0 || isNaN == true || r == size && θ == angle){
                    if(n != 0 && (θ != angle || isNaN==false)){
                        System.out.println("新しいメッシュが作成されました"+n);
                    
                    
                    
                    }
                    
                    isNaN = false;
                }

                // 頂点の作成
                float radian = (float) Math.toRadians(θ);
                float xPos = r * (float) Math.cos(radian);
                float yPos = r * (float) Math.sin(radian);
                float zPos = (float)FunctionSimpler.simpler("x/y", r, θ).evalf();// 高さを計算
                
                // テクスチャ座標の作成
                float u = θ/360f;
                float v = r/(float)(size+1);
                
                

                // NaNを検出
                try{
                    if(Float.isNaN(zPos)){
                        throw new IllegalArgumentException("zPos is NaN");
                    }
                }catch(IllegalArgumentException e){
                    System.out.println("NaNが検出されました");
                    zPos = CreateMesh.MaxMinDivider((float)FunctionSimpler.simpler("x/y", r, θ-1).evalf(),size);
                    isNaN = true;
                    n=0;
                    continue;//座標だけは保存される。
                }finally{

                System.out.println("x= "+xPos+" y= "+yPos+" z= "+zPos);
                System.out.println(" u= " +u+" v= "+v);
                    
                }
                
                if(r != size && θ != angle){
                // 三角形の作成
                int p00 = θ*(size+1)+r;
                int p01 = θ*(size +1)+r+1;
                int p10 = (θ+1)*(size +1)+r;
                int p11 = (θ+1)*(size +1)+r+1;

                if(aspect == 1){// 表面
                System.out.println("インデクス " + p00+ " "+ p00+" "+p10+" "+ p10+" "+ p01+" "+ p01);
                System.out.println(p10+" "+ p10+" "+ p01+" "+ p01+" "+p11+" "+ p11);
                }else if(aspect == 0){// 裏面
                    System.out.println(p00+" "+ p00+" "+ p01+" "+ p01+" "+ p10+" "+ p10);
                    System.out.println(p10+" "+ p10+" "+ p11+" "+ p11+" "+ p01+" "+ p01);
                }
                }
                
                
                n += 1;
                
                
            
            }
        
    
        }
            


    }
}


    
