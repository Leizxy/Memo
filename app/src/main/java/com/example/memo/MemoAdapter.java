package com.example.memo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @date 12/8/20
 * @description
 */
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.Holder> {
    private Context context;
    private List<MemoBean> data;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public MemoAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_memo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<MemoBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView title, type, time;
        private MemoBean bean;

        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddActivity.class);
                intent.putExtra("modify", true);
                intent.putExtra("id", bean.getId());
                context.startActivity(intent);
            });
        }

        public void setData(MemoBean memoBean) {
            this.bean = memoBean;
            title.setText(memoBean.getContent());
            type.setText(App.getInstance().getHelper().getCategory(memoBean.getCategory()));
            time.setText(format.format(new Date(memoBean.getTime())));
        }
    }
}
