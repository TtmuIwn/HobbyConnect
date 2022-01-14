package com.stickynote.ttmiwn.JsoupStep22;

public class SerchWordProcess {
    
    String[] SerchWordProcess(String str){

        if (str.isEmpty()) {
            System.out.println("文字列は空です");
            //return hoge; (-> set.text("検索ワードを入力して下さい");
        }

        //前後の空白の削除 trim
        str = str.replaceAll("　", " ");
        str = str.trim();
        String[] strs = str.split(" ");

        for( String s : strs) {
            System.out.println(s);
        }
        return strs;
    }
}
