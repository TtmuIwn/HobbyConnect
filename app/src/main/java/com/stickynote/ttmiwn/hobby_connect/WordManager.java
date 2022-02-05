package com.stickynote.ttmiwn.hobby_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

//データベース管理画面
public class WordManager extends AppCompatActivity {

    //    削除iDの番号　SQLに渡す
    String selectID = "";
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_manager);

        TextView textView = findViewById(R.id.selectWord);

        ListDisp();

//        削除BT） リストとビューで選択した単語を削除
        Button deleBt = findViewById(R.id.deleteBt);
        deleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper _helper = new DatabaseHelper(WordManager.this);
                SQLiteDatabase db = _helper.getWritableDatabase();

                String sqlDelete = "DELETE FROM serchwords WHERE _id = ?";
                SQLiteStatement stmt = db.compileStatement(sqlDelete);
                stmt.bindLong(1, Long.parseLong(selectID));

                stmt.executeUpdateDelete();

                ListDisp();
            }
        });

        //リストビューをタップした時の各行のデータを取得
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                selectID = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                textView.setText(((TextView)view.findViewById(android.R.id.text2)).getText().toString());
            }
        });


//        全削除BT） DBテーブル削除　ワンクッションのダイアログ表示
        Button allDelete = findViewById(R.id.allDeleteBt);
        allDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmDialog cfd = new ConfirmDialog();
                cfd.show(getSupportFragmentManager(), "ConfirmDialog");

            }
        });

//        戻る） メイン画面に戻る
        Button backBt = findViewById(R.id.backBt);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    データベースの中身を表示
    public void ListDisp(){
        DatabaseHelper _helper = new DatabaseHelper(WordManager.this);
        SQLiteDatabase db = _helper.getWritableDatabase();

        Cursor cursor = db.query("serchwords", null, null, null, null, null, null);

//        LIｓｔView用の表示
        String[] from = {"_id", "word" };
        int[] to = {android.R.id.text1,android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(WordManager.this,android.R.layout.simple_list_item_2,cursor,from,to,0);

        //バインドして表示
        myListView = findViewById(R.id.listview);
        myListView.setAdapter(adapter);

    }
}
