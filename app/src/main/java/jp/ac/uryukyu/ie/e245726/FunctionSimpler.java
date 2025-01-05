package jp.ac.uryukyu.ie.e245726;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

public class TrigonometricRatio {
    public static void main(String[] args) {
        // SymjaのExprEvaluatorを作成
        ExprEvaluator evaluator = new ExprEvaluator();

        // 三角関数の簡略化
        String expression = "Simplify(Sin(x) / Cos(x))";
        IExpr result = evaluator.eval(expression);
        System.out.println("式: " + expression);
        System.out.println("結果: " + result); // 結果: 1

        // 微分計算
        String derivativeExpression = "D(Sin(x), x)";
        IExpr derivative = evaluator.eval(derivativeExpression);
        System.out.println("式: " + derivativeExpression);
        System.out.println("微分: " + derivative); // 結果: Cos(x)

        // 極座標変換例
        String polarExpression = "TrigExpand(Sin(2*theta))";
        IExpr polarResult = evaluator.eval(polarExpression);
        System.out.println("式: " + polarExpression);
        System.out.println("結果: " + polarResult); // 結果: 2*Cos(theta)*Sin(theta)
    }
    
}
