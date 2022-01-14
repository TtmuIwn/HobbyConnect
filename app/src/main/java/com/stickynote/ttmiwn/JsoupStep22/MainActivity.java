package com.stickynote.ttmiwn.JsoupStep22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 引き抜くワード　wikipedia　動画　通販　楽天　youtube　「について」「でわかる」「今さら聞けない」「とは」


public class MainActivity extends AppCompatActivity {
//    スクレイピング検索結果保持用　webValuesは丸々テキストなので消して良いかも
    private static String webValues;
    private static List<String> strlist = new ArrayList<>();

    // url 作成用
    private static String[] kensaku ;
    static String preurl = "";
    String url = "https://www.google.com/search?q=";

    static String heppoko = "ororo";

//  　データベース用オブジェクト
    private DatabaseHelper _helper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.editText);
        TextView output = findViewById(R.id.serchTx);

        //　webValues入れる下部のテキスト　消すかなー
        TextView textView = findViewById(R.id.textview);

//        検索ワード、登録ボタンとクリアボタンの処理クラスをnew

//        DBヘルパーオブジェクトを生成。
        _helper = new DatabaseHelper(MainActivity.this);
        db = _helper.getWritableDatabase();

//        戻ってきた時の表示テスト用
        output.setText(heppoko);

//        結果表示のリストビュー
        ListView mylistView = findViewById(R.id.listview);

        //        クリアボタン
        Button btClear = findViewById(R.id.clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
                output.setText("");
                preurl = "";
            }
        });


//        画面遷移）　単語マネージャーに飛ぶボタン
        Button manageBt = findViewById(R.id.manage);
        manageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WordManager.class);
                //なんか持ってくもの単語とか合ったらput
                startActivity(intent);
            }
        });



        //ｄｂとうろくしょり
        Button dbPushBt = findViewById(R.id.datapushBt);
        dbPushBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SQLiteDatabase db = _helper.getWritableDatabase();
                ContentValues values = new ContentValues();


                for(int i =0 ; kensaku.length >i ; i++) {
                    String str = kensaku[i];
                    values.put("word", str);
                    db.insert("serchwords", null, values);
                }

            }
        });

        //      表示ボタン、クリック時の処理
        Button buttonHyouji = findViewById(R.id.displayBt);
        buttonHyouji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(webValues);

//               リストビューの設定
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,strlist);
                mylistView.setAdapter(adapter);

                //  リサイクルビューの設定 のちのちこっちに変える
//                RecyclerView recyclerView = findViewById(R.id.recyclerView);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                recyclerView.setLayoutManager(layoutManager);
//                RecyclerView.Adapter mAdapter = new MyAdapter(strlist);
//                recyclerView.setAdapter(mAdapter);
            }
        });


//        chromeで検索
        Button browser = findViewById(R.id.goBrowser);
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlfull = url + preurl;
                Intent intent = new Intent();
                intent.setData( Uri.parse(urlfull));
                startActivity(intent);
            }
        });

//        リストビュークリック処理
        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) mylistView.getItemAtPosition(position);
                EditText edt = findViewById(R.id.editText);
                edt.setText(str);
            }
        });
    }


//    検索ボタンクリック時の処理　＆Jsoupスクレイピング
    public void serchJsoup(View view) {

        EditText input = findViewById(R.id.editText);
        TextView output = findViewById(R.id.serchTx);

        // 入力された文字をgetする　inputstrを　加工する処理
        String inputStr = input.getText().toString();

        SerchWordProcess swp = new SerchWordProcess();
        kensaku = swp.SerchWordProcess(inputStr);
        preurl = "";

        for(int i =0 ; kensaku.length >i ; i++) {
            preurl = preurl + kensaku[i] + "+";
        }

        output.setText("『" + inputStr + "』を検索");
//                    preurl = input.getText().toString();


        String urlfull = url + preurl;

        Looper mainLooper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);
        BackgroundJsoup bgj = new BackgroundJsoup(handler, urlfull);
//                ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(bgj);
        // 表示ボタンをタップできるように設定。
        Button btnSave = findViewById(R.id.displayBt);
        btnSave.setEnabled(true);

    }




    //    データベースからランダムに単語を抽出し、EditTextにセットする
    public void RandomWord(View view) {

        Cursor cursor = db.query("serchwords", null, null, null, null, null, null);

        cursor.moveToFirst();


//        araylistで受け取る
        ArrayList<String> list = new ArrayList<>();

        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
    //            sbuilder.append(cursor.getString(1));
    //            sbuilder.append(" ");
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }

        // 忘れずに！
        cursor.close();

        Randomsampling rds = new Randomsampling();
        sbuilder = rds.ListRandomsampling(list);
        EditText edt = findViewById(R.id.editText);
        edt.setText(sbuilder.toString());

    }

    //    backTaskからのセッター二種
    public static void setValues(String s) {

        webValues = s;

    }
    public static void setStrarray(List<String> list) {

        strlist = list;
    }

//      バック処理終了後うごかしたい
//    public void hyouji(){
//        ListView mylistView = findViewById(R.id.listview);
//
//        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,strlist);
//        mylistView.setAdapter(adapter);
//    }

//    管理画面から戻ってきた際の処理　不要になるかも
    @Override
    protected void onRestart(){
        TextView output = findViewById(R.id.serchTx);
        output.setText(heppoko);
        super.onRestart();
    }

}
