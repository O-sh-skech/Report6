# Report6
小作品課題です
1st commit z=y/xの3Dモデルを仮実装
2st commit z=y/xの3Dモデルを極座標変換で仮実装。他の関数に対応するために、UIを追加する前段階
3st commit UIを仮設置し、他関数もおおよそ対応できるように。発散の対応の前段階。
4th commit テクスチャ周りを整理。画像を適応できるようにした。発散の際にメッシュを分割できれば3Dは完成。
5th commit θ方向の発散でメッシュを分割できるようにした。r方向は未実装。
6th commit θ,r方向ともに実装。3Dは完成した。
7th commit UIを整えて、アニメーションを追加してみた。
8th commit x,yの文字を挿入、回転UIを修正、javaDocを整理した。
9th commit javaDoc,jarファイル生成実行、JUNITテスト実行確認
## 概要
このアプリケーションは任意のX,Yの2変数関数を極座標変換した関数グラフィックを可視化します。
X,Y平面の円を基にしてZの値を考えることができるので、アイデアよりも体系的な処理を重視した作図方法です。
## 使用した外部ライブラリ
Symja https://github.com/andryr/symja 関数の極座標変換、および簡略化
javaFX https://openjfx.io 3Dグラフィックの生成
FXyz https://github.com/FXyz/FXyz/tree/master 3Dテキストの生成