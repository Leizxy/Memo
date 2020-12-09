package com.example.memo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @date 12/9/20
 * @description
 */
public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.Holder> {
    private Context context;
    private List<MemoTypeBean> data;

    public TypeAdapter(Context context) {
        this.context = context;
        data = App.getInstance().getHelper().getCategories();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData() {
        data = App.getInstance().getHelper().getCategories();
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView del;
        private MemoTypeBean bean;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            del = itemView.findViewById(R.id.del);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (bean.isShowDel()) {
                        del.setVisibility(View.GONE);
                        bean.setShowDel(false);
                    } else {
                        del.setVisibility(View.VISIBLE);
                        bean.setShowDel(true);
                    }
                    return false;
                }
            });
            del.setOnClickListener(view -> {
                App.getInstance().getHelper().deleteCategory(bean.getId());
                updateData();
            });
        }

        public void setData(MemoTypeBean memoTypeBean) {
            this.bean = memoTypeBean;
            name.setText(bean.getName());
            del.setVisibility(bean.isShowDel() ? View.VISIBLE : View.GONE);
        }
    }
}
