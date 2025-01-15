package jp.ac.uryukyu.ie.e245726;
import java.util.ArrayList;

public class CopyList<T> { // クラスにジェネリクス<T>を追加
    private ArrayList<T> list = new ArrayList<>();

    public CopyList(ArrayList<T> list) { // コンストラクタでも<T>を使う
        this.list = new ArrayList<>(list); // 渡されたリストをコピーする
    }

    public ArrayList<T> getList() { // リストを取得するメソッド
        return list;
    }
}
