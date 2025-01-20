package jp.ac.uryukyu.ie.e245726;
/*
 * This source file was generated by the Gradle 'init' task
 */
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class Graphic3DTest {
    @Test
    void ListIndexTest(){
        ArrayList<ArrayList<ArrayList<float[]>>> outerWall = new ArrayList<>();
        ArrayList<ArrayList<float[]>> innerWall = new ArrayList<>();
        ArrayList<float[]> wall = new ArrayList<>();
        innerWall.add(wall);
        assertEquals(innerWall.size(), 1);
    }       

    @Test
    void ListIndexTest2(){
        ArrayList<ArrayList<ArrayList<float[]>>> outerWall = new ArrayList<>();
        ArrayList<float[]> wall = new ArrayList<>();
        for (int r = 0; r <= 3; r++) {
            ArrayList<ArrayList<float[]>> innerWall = new ArrayList<>();
        for (int θ = 0; θ <= 2; θ++){
            innerWall.add(wall);
            }
            assertEquals(innerWall.size(), 3);
            outerWall.add(innerWall);
        }
        assertEquals(outerWall.size(), 4);
    }

    @Test
    void indexSomTest() throws Exception {
            CreateMesh calculator = new CreateMesh();
    
            // private メソッドを取得
            Method method = CreateMesh.class.getDeclaredMethod("adjustZPos", float.class, int.class, String.class, int.class, boolean.class);
            method.setAccessible(true); // private アクセスを許可
    
            // private メソッドを実行
            float result = (float) method.invoke(calculator,0,0,"x/y",2,false);
            float compared = (float)FunctionSimpler.simpler("x/y", 0, 0-1).getResult().evalf();
            assertEquals(result, Math.max(compared,-2*10^5));
        }
    }

    

    

