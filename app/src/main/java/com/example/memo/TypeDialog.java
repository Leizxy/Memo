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
public class TypeDialog extends Dialog {

    public TypeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    static class Builder {
        private Context context;
        private OnClick click;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setClick(OnClick click) {
            this.click = click;
            return this;
        }

        public TypeDialog create() {
            TypeDialog dialog = new TypeDialog(context, R.style.dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_type, null);
            dialog.setContentView(view);
            EditText et = view.findViewById(R.id.et);
            view.findViewById(R.id.confirm).setOnClickListener(v -> {
                if (click != null) {
                    click.clickOk(et.getText().toString().trim());
                }
                dialog.dismiss();
            });
            view.findViewById(R.id.cancel).setOnClickListener(v -> {
                dialog.dismiss();
            });
            return dialog;
        }
    }

    public interface OnClick {
        void clickOk(String name);
    }
}
