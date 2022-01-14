package com.stickynote.ttmiwn.JsoupStep22;

import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.WorkerThread;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//　非同期処理を行うクラス　url　を受け取りスクレイピング　結果listをセッターで登録

public class BackgroundJsoup implements Runnable {

    private final Handler _handler;
    private final String _urlFull;

    String DEBUG_TAG;

    public BackgroundJsoup(Handler handler , String urlFull) {
        _handler = handler;
        _urlFull = urlFull;
    }

    @WorkerThread
    @Override
    public void run() {
        // HTTP接続を行うHttpURLConnectionオブジェクトを宣言。finallyで解放するためにtry外で宣言。
        HttpURLConnection con = null;

        String text = "";
        List<String> list = new ArrayList<>();
        Document doc = null;

        try {
            // URLオブジェクトを生成。
            URL url = new URL(_urlFull);
            // URLオブジェクトからHttpURLConnectionオブジェクトを取得。
            con = (HttpURLConnection) url.openConnection();
//            // 接続に使ってもよい時間を設定。
//            con.setConnectTimeout(1000);
//            // データ取得に使ってもよい時間。
//            con.setReadTimeout(1000);
            // HTTP接続メソッドをGETに設定。
            con.setRequestMethod("GET");
            // 接続。
            con.connect();

            final int statusCode = con.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                System.err.println("正常に接続できていません。statusCode:" + statusCode);
            }

            // Jsoupで対象URLの情報を取得する。
            try {
                doc = Jsoup.connect(_urlFull).get();
                Elements elements = doc.select("h3");
                for (Element element : elements) {
                    list.add(element.text());
                }
                text = doc.text();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch(MalformedURLException ex) {
            Log.e(DEBUG_TAG, "URL変換失敗", ex);
        }
        // タイムアウトの場合の例外処理。
        catch(SocketTimeoutException ex) {
            Log.w(DEBUG_TAG, "通信タイムアウト", ex);
        }
        catch(IOException ex) {
            Log.e(DEBUG_TAG, "通信失敗", ex);
        }
        finally {
            // HttpURLConnectionオブジェクトがnullでないなら解放。
            if(con != null) {
                con.disconnect();
            }
        }

        MainActivity.setValues(text);
        MainActivity.setStrarray(list);
    }
}