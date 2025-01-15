package jp.ac.uryukyu.ie.e245726;


import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;



public class FunctionSimpler {
    private int functionType;
    private IExpr result;
    FunctionSimpler(int functionType, IExpr result){
        this.functionType = functionType;
        this.result = result;
    }
    public int getFunctionType(){
        return this.functionType;
    }
    public IExpr getResult(){
        return this.result;
    }

    public static FunctionSimpler simpler(String function, float r, int θ) {
        int functionType = 0;
        // ExprEvaluatorを作成
        ExprEvaluator evaluator = new ExprEvaluator();

        
        // r*cos(θ)とr*sin(θ)を設定
        evaluator.eval("x = r * cos(angle)");
        evaluator.eval("y = r * sin(angle)");

        // function（zPosの式）を設定
        IExpr changed = evaluator.eval("z = " + function);
        if(changed.toString().contains("r")){
            if(changed.toString().contains("θ")){
                functionType = 2;//rとθの関数
            }else{
                functionType =1;//rの関数
            }
        }else if(changed.toString().contains("θ")){
            functionType = 0;//θの関数

        }
        

        // 変数θをSymjaに設定
         evaluator.eval("angle = " + θ + " Degree");
        // 変数rをSymjaに設定
        evaluator.eval("r ="+ r);
        
            // 結果を取得
        IExpr result = evaluator.eval("z");

        if (result.toString().equals("ComplexInfinity")) {
            return new FunctionSimpler(functionType,F.num(Double.NaN));
        }else{
            return new FunctionSimpler(functionType,result);
        }
    }
    public static void main(String[] args) {
    }
}


    
