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

//管理画面にて、全消去が押された際に表示するダイアログを生成

public class ConfirmDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_ok, new DialogButtonClickListener())
                .setNegativeButton(R.string.dialog_ng, new DialogButtonClickListener());

        AlertDialog dialog = builder.create();
        return dialog;
    }

//    ダイアログのボタンがタップされた際の処理

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            String msg = "";

            switch (which) {

//                ポシティブボタン） テーブル削除処理
                case DialogInterface.BUTTON_POSITIVE:

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

                case DialogInterface.BUTTON_NEGATIVE:

                    msg = getString(R.string.dialog_ng_toast);
                    break;
            }
            //トーストの表示。
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }
}
