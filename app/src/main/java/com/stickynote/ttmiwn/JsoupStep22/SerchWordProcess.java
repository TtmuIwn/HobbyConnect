package com.stickynote.ttmiwn.JsoupStep22;

//  全角スペースを半角スペースに変換後、trimして分割
// 、, 等への対応ほか、記号への追記など必要か

public class SerchWordProcess {
    
    String[] SerchWordProcess(String str){

        str = str.replaceAll("　", " ");
        str = str.trim();
        String[] strs = str.split(" ");

        return strs;
    }
}
