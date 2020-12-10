package com.example.memo;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

/**
 * @author Created by wulei
 * @date 2020/12/10, 010
 * @description
 */
public class ChooseDialog extends Dialog {
    public ChooseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private OnClick click;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setClick(OnClick click) {
            this.click = click;
            return this;
        }

        public ChooseDialog create() {
            ChooseDialog dialog = new ChooseDialog(context, R.style.dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose, null);
            dialog.setContentView(view);
            view.findViewById(R.id.camera).setOnClickListener(v -> {
                if (click != null) {
                    click.clickCamera();
                }
                dialog.dismiss();
            });
            view.findViewById(R.id.choose).setOnClickListener(v -> {
                if (click != null) {
                    click.clickPhoto();
                }
                dialog.dismiss();
            });
            return dialog;
        }
    }

    public interface OnClick {
        void clickCamera();

        void clickPhoto();
    }
}
