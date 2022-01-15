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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MainActivity extends AppCompatActivity {
//    スクレイピング検索結果保持用
    private static List<String> h3Captions = new ArrayList<>();

    // url 作成用
    private static String[] serchWords ;
    private static String preURL = "";
    private final String url = "https://www.google.com/search?q=";

//    検索単語、確認用
    static String thisWords = "";

//  　データベース用オブジェクト
    private DatabaseHelper _helper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        単語入力用 EDIT　検索後家訓用 TEXT 検索結果表示 リストビュー
        EditText input = findViewById(R.id.editText);
        TextView output = findViewById(R.id.serchTx);
        ListView mylistView = findViewById(R.id.listview);

//        DBヘルパーオブジェクトを生成。
        _helper = new DatabaseHelper(MainActivity.this);
        db = _helper.getWritableDatabase();


//        クリアボタン) EDIT & TEXT 内容のクリア処理
        Button btClear = findViewById(R.id.clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
                output.setText("");
                preURL = "";
            }
        });


//        管理ボタン） データベース管理クラスに画面遷移
        Button manageBt = findViewById(R.id.manage);
        manageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WordManager.class);
                startActivity(intent);
            }
        });


//        単語登録ボタン） データベースに単語を登録
        Button dbPushBt = findViewById(R.id.datapushBt);
        dbPushBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SQLiteDatabase db = _helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                for(int i =0 ; serchWords.length >i ; i++) {
                    String str = serchWords[i];
                    values.put("word", str);
                    db.insert("serchwords", null, values);
                }
            }
        });

//        表示ボタン） スクレイピング結果を表示する
        Button dispBt = findViewById(R.id.displayBt);
        dispBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(h3Captions.size() == 0){

                    Toast.makeText(MainActivity.this, "もう一度タップして下さい",Toast.LENGTH_SHORT).show();

                } else {

                    ArrayAdapter adapter = new ArrayAdapter
                            (MainActivity.this, android.R.layout.simple_list_item_1, h3Captions);
                    mylistView.setAdapter(adapter);
//                    h3Captions.clear();
                    dispBt.setEnabled(false);
                }
            }
        });


//        外部ブラウザボタン） 暗黙インデント chromeで検索
        Button browser = findViewById(R.id.goBrowser);
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlfull = url + preURL;
                Intent intent = new Intent();
                intent.setData(Uri.parse(urlfull));
                startActivity(intent);
            }
        });

//        リストビュークリック） 検索結果を処理
        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) mylistView.getItemAtPosition(position);
                EditText edt = findViewById(R.id.editText);
                edt.setText(str);
            }
        });
    }


//    検索ボタンクリック） EDIT内の検索ワードを ＆Jsoupスクレイピング
    public void serchJsoup(View view) {

        EditText input = findViewById(R.id.editText);
        TextView output = findViewById(R.id.serchTx);

        String inputStr = input.getText().toString();

//        全角・半角スペースでトリミング
        SerchWordProcess swp = new SerchWordProcess();
        serchWords = swp.SerchWordProcess(inputStr);

        if(serchWords.length == 1 && serchWords[0].equals("") ){
            input.setHint("↑ランダムワード or 検索単語を入力");
        } else {

            preURL = "";

            for (int i = 0; serchWords.length > i; i++) {
                preURL = preURL + serchWords[i] + "+";
            }

            output.setText("『 " + inputStr + " 』を検索");
//                    preURL = input.getText().toString();

            String urlfull = url + preURL;

            Looper mainLooper = Looper.getMainLooper();
            Handler handler = HandlerCompat.createAsync(mainLooper);
            BackgroundJsoup bgj = new BackgroundJsoup(handler, urlfull);
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(bgj);

            // 表示ボタンをタップできるように設定。
            Button btnSave = findViewById(R.id.displayBt);
            btnSave.setEnabled(true);
        }
    }


//    最上部、ランダムボタン) データベースからランダムに単語を抽出し、EditTextにセットする
    public void RandomWord(View view) {

        Cursor cursor = db.query("serchwords", null, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        EditText edt = findViewById(R.id.editText);
//        登録単語が　ｎ（3）以下の場合
        if(list.size() < 3){
            list.add("wikipedeiaおまかせ表示");
            String msg = "単語を3個以上登録して下さい";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            edt.setText("wikipedeiaおまかせ表示");
        } else {
            Randomsampling rds = new Randomsampling();
            String str = rds.ListRandomsampling(list);
            edt.setText(str);
        }
    }

//    BackgroundJsoup からのセッター
    public static void setStrarray(List<String> list) {
        h3Captions = list;
    }
}
