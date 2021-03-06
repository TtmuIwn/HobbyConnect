package com.stickynote.ttmiwn.hobby_connect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//　初回データベース作成　バージョンUPやダウン未対応

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "serchword.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        // 親クラスのコンストラクタの呼び出し。
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成用SQL文字列の作成。
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE serchwords (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("word TEXT,");
        sb.append("unique(word)");
        sb.append(");");
        String sql = sb.toString();

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
