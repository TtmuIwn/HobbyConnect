package com.stickynote.ttmiwn.hobby_connect;

import java.util.ArrayList;
import java.util.Collections;

//受け取った単語Listをランダムに並び替え、指定した個数戻す（基本 3語）

public class Randomsampling {

    String ListRandomsampling(ArrayList<String> list){

        Collections.shuffle(list);
        StringBuilder stb = new StringBuilder();

        for(int i=0 ; i<3 ; i++) {
            String str = list.get(i);
            stb.append(str + " ");
        }

        return stb.toString();
    }
}
