package jp.ac.uryukyu.ie.e245726;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

public class FunctionSimpler {
    public static IExpr simpler(String function, int r, float angle) {
        // ExprEvaluatorを作成
        ExprEvaluator evaluator = new ExprEvaluator();
        
        // 変数θをSymjaに設定
        evaluator.eval("angle = " + angle);
        
        // r*cos(θ)とr*sin(θ)を設定
        evaluator.eval("x = r * cos(angle)");
        evaluator.eval("y = r * sin(angle)");

        // function（zPosの式）を設定
        evaluator.eval("z = " + function);

        // 変数rをSymjaに設定
        evaluator.eval("r ="+ r);

        // 結果を取得
        IExpr result = evaluator.eval("z");
        
        return result;
    }

    public static void main(String[] args) {
        for(int θ = 0; θ < 360; θ++){

        float angle = (float) Math.toRadians(θ);
        // 結果の表示
        IExpr result = simpler("y/x",0, angle);
        System.out.println((float)result.evalf());
        }
        
        
        }
}
