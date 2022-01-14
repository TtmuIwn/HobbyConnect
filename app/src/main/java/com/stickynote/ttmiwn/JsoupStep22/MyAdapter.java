package com.stickynote.ttmiwn.JsoupStep22;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//    private String[] myDataset;
    private List<String> hoge = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

//    public MyAdapter(String[] myDataset) {
//        this.myDataset = myDataset;
//    }

    public MyAdapter(List<String> list) {
        this.hoge.addAll(list);
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        TextView v = new TextView(parent.getContext());
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //メニュー名文字列を取得。
        String fuga = (String) hoge.get(position);
        holder.textView.setText(fuga);
    }

    @Override
    public int getItemCount() {
        return hoge.size();
    }
}
