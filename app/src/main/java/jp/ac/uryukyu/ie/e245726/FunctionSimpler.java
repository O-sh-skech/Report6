package jp.ac.uryukyu.ie.e245726;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

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
        for (int θ = 0; θ < 15; θ++){

            IExpr result = FunctionSimpler.simpler("x/y", 1, θ);// 高さを計算
            if (result.toString().equals("ComplexInfinity")) {
                IExpr result1 = FunctionSimpler.simpler("x/y", 1, θ+1);
                System.out.println((float)result1.evalf());
               
            }else{
                System.out.println((float)result.evalf());
            }
        }
        //ExprEvaluator F = new ExprEvaluator();
        //F.eval("x = " + (float) Math.toRadians(90));
        //IExpr result = F.eval("1/0");

        //System.out.println((float)result.evalf()); 
    }
}

    
