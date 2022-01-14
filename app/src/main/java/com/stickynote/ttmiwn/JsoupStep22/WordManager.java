package com.stickynote.ttmiwn.JsoupStep22;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WordManager extends AppCompatActivity {

//    削除iDの番号　SQLに渡す
    String deleteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_manager);

        Intent intent = getIntent();

        ListView myListView = findViewById(R.id.listview);

        Button hyouji = findViewById(R.id.mgbt);
        hyouji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper _helper = new DatabaseHelper(WordManager.this);
                SQLiteDatabase db = _helper.getWritableDatabase();

                Cursor cursor = db.query("serchwords", null, null, null, null, null, null);

//        LIｓｔView用の表示
                String[] from = {"_id", "word" };
                int[] to = {android.R.id.text1,android.R.id.text2};
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(WordManager.this,android.R.layout.simple_list_item_2,cursor,from,to,0);

                //バインドして表示
                myListView.setAdapter(adapter);

            }

        });

        //リストビューをタップした時の各行のデータを取得
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
            deleteId = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
          }
        });

        Button deleBt = findViewById(R.id.deleteBt);
        deleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
                DatabaseHelper _helper = new DatabaseHelper(WordManager.this);
                SQLiteDatabase db = _helper.getWritableDatabase();

                // 削除用SQL文字列を用意。
                String sqlDelete = "DELETE FROM serchwords WHERE _id = ?";
                // SQL文字列を元にプリペアドステートメントを取得。
                SQLiteStatement stmt = db.compileStatement(sqlDelete);
                // 変数のバイド。
                stmt.bindLong(1, Long.parseLong(deleteId));
                // 削除SQLの実行。
                stmt.executeUpdateDelete();

            }
        });


//        データベース全消去
        Button allDelete = findViewById(R.id.allDeleteBt);
        allDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmDialog cfd = new ConfirmDialog();
                cfd.show(getSupportFragmentManager(), "ConfirmDialog");

//                DatabaseHelper _helper = new DatabaseHelper(WordManager.this);
//                SQLiteDatabase db = _helper.getWritableDatabase();
//
//                String sql = "DELETE FROM serchwords";
//
//                try {
//                    db.execSQL(sql);
//                } catch (SQLException e) {
//                    Log.e("ERROR", e.toString());
//                }

            }
        });

//        メイン画面に戻る
        Button backBt = findViewById(R.id.backBt);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}

