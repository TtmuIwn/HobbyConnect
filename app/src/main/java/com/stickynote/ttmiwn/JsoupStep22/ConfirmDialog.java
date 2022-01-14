package com.stickynote.ttmiwn.JsoupStep22;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //ダイアログビルダを生成。
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ダイアログのタイトルを設定。
        builder.setTitle(R.string.dialog_title)
                //ダイアログのメッセージを設定。
                .setMessage(R.string.dialog_message)
                //Positive Buttonを設定。
                .setPositiveButton(R.string.dialog_ok, new DialogButtonClickListener())

                //注文用のメッセージを格納。
//                    DatabaseHelper _helper = new DatabaseHelper(WordManager.this);
//                    SQLiteDatabase db = _helper.getWritableDatabase();
//
//                    String sql = "DELETE FROM serchwords";
//
//                    try {
//                        db.execSQL(sql);
//                    } catch (SQLException e) {
//                        Log.e("ERROR", e.toString());
//                    }

                //Negative Buttonを設定。
                .setNegativeButton(R.string.dialog_ng, new DialogButtonClickListener());

        //ダイアログオブジェクトを生成し、リターン。
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * ダイアログのアクションボタンがタップされた時の処理が記述されたメンバクラス。
     */
    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //トーストメッセージ用文字列変数を用意。
            String msg = "";
            //タップされたアクションボタンで分岐。
            switch (which) {
                //Positive Buttonならば…
                case DialogInterface.BUTTON_POSITIVE:

                    //注文用のメッセージを格納。
                    DatabaseHelper _helper = new DatabaseHelper(getActivity());
                    SQLiteDatabase db = _helper.getWritableDatabase();

                    String sql = "DELETE FROM serchwords";

                    try {
                        db.execSQL(sql);
                    } catch (SQLException e) {
                        Log.e("ERROR", e.toString());
                    }



                    msg = getString(R.string.dialog_ok_toast);
                    break;

                //Negative Buttonならば…
                case DialogInterface.BUTTON_NEGATIVE:
                    //キャンセル用のメッセージを格納。
                    msg = getString(R.string.dialog_ng_toast);
                    break;
            }
            //トーストの表示。
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }
}
