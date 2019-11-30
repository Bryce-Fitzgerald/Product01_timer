package com.example.timer03.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.timer03.R;

import java.util.List;

//自定义RecycleView适配器
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    //设定将要展示在视图中的列表
    private List<String> mList;
    //?
    private Context mContext;

    //构造方法
    //?
    public MyRecyclerViewAdapter(Context context, List<String> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.getTextView().setText(getItem(position));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //最后添加的显示在最上面
    public String getItem(int pos) {
        return mList.get(getItemCount() -1 - pos);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private View view;
        public TextView getTextView() {
            return textView;
        }
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) view.findViewById(R.id.text_view);
        }
    }
}
