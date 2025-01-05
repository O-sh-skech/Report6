package jp.ac.uryukyu.ie.e245726;

import javafx.scene.shape.TriangleMesh;

public class Mesh {
    /**
     * TriangleMesh を作成する
     * @param size 円の半径
     * @param aspect 面の裏表
     * @return TriangleMesh
     */
    public static TriangleMesh createSurfaceMesh(int size, int aspect) {//aspectは０（裏）か１（表）
        TriangleMesh mesh = new TriangleMesh();
        // 頂点の作成
        for (int r = 0; r <= size; r++) {
            for (int θ = 0; θ < 80; θ++){
                float angle = (float) Math.toRadians(θ);
                float xPos = r * (float) Math.cos(angle);
                float yPos = r * (float) Math.sin(angle);
                float zPos = (float)4*(float)(yPos)/(float)(xPos);// 高さを計算

                mesh.getPoints().addAll(xPos, zPos, yPos);
            }
        }

        // テクスチャ座標の作成（ダミー）
        for (int r = 0; r <= size; r++) {
            for (int θ = 1; θ <= 360; θ++) {
                float u = θ*r / 360*size;
                float v = θ*r / 360*size;
                mesh.getTexCoords().addAll(u, v);
            }
        }

        // 三角形の作成
        for (int r = 0; r < size; r++) {
            for (int θ = 0; θ < 80; θ++) {
                int p00 = r * 80 + θ;
                int p01 = r * 80 + (θ + 1)%80;
                int p10 = (r + 1) * 80 + θ;
                int p11 = (r + 1) * 80 + (θ + 1)%80;

                if(aspect == 1){//表面
                    // 上の三角形
                mesh.getFaces().addAll(p00, 0, p10, 0, p01, 0);

                    // 下の三角形
                mesh.getFaces().addAll(p10, 0, p11, 0, p01, 0);
                }else if(aspect == 0){//裏面
                    // 上の三角形
                mesh.getFaces().addAll(p00, 0, p01, 0, p10, 0);

                    // 下の三角形
                mesh.getFaces().addAll(p10, 0, p01, 0, p11, 0);

                }

                
            }
        }
        return mesh;
    }
}
