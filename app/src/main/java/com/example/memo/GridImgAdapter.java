package com.example.memo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 图片显示 adapter
 */
public class GridImgAdapter extends RecyclerView.Adapter {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;


    private Context context;
    private int selectMax = 6;
    private List<Object> list;
    private Callback callback;

    public GridImgAdapter(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        list = new ArrayList<>();
    }


    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            return new AddHolder(LayoutInflater.from(context).inflate(R.layout.main_item_grid_add, parent, false));
        } else {
            return new ImgHolder(LayoutInflater.from(context).inflate(R.layout.main_item_grid_img, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddHolder) {
        } else if (holder instanceof ImgHolder) {
            if (list.get(position) instanceof String) {
                ((ImgHolder) holder).loadImg(position);
//            } else if (list.get(position) instanceof LocalMedia) {
//                ((ImgHolder) holder).loadImg(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    //判断类别
    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    //图片是否为空
    public boolean isEmpty() {
        return list.isEmpty();
    }

    //添加图片
    public void addImg(String imagePath) {
        this.list.add(imagePath);
        notifyDataSetChanged();
    }

    public String getImgs() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object str : list) {
            stringBuilder.append(str);
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    //获取图片字符串
    //用于添加进数据库
    public String getImgsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }


    class AddHolder extends RecyclerView.ViewHolder {
        ImageView addInfo;

        public AddHolder(@NonNull View itemView) {
            super(itemView);
            addInfo = itemView.findViewById(R.id.item_add_info);
            itemView.setOnClickListener(v -> {
                if (callback != null) {
                    callback.addPic();
                }
            });
        }
    }

    class ImgHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageView imgDel;
        LinearLayout del;
        private int position;

        public ImgHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_iv);
            imgDel = itemView.findViewById(R.id.item_iv_del);
            del = itemView.findViewById(R.id.item_del);
            del.setOnClickListener(this::onViewClicked);
        }

        public void loadImg(int position) {
            this.position = position;
            del.setVisibility(View.VISIBLE);
            img.setImageBitmap(BitmapFactory.decodeFile((String) list.get(position)));
        }

        public void onViewClicked(View view) {
            int i = view.getId();
            if (i == R.id.item_del) {
                list.remove(position);
                notifyDataSetChanged();
            }
        }
    }

    public interface Callback {
        void addPic();
    }
}
