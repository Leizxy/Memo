package com.example.memo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @date 12/8/20
 * @description
 */
public class TypesActivity extends AppCompatActivity {
    private TypeAdapter typeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
        setTitle("事件类别管理");
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        typeAdapter = new TypeAdapter(this);
        rv.setAdapter(typeAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                TypeDialog dialog = new TypeDialog.Builder(this).setClick(name -> {
                    if (!TextUtils.isEmpty(name)) {
                        App.getInstance().getHelper().addCategory(name);
                        typeAdapter.updateData();
                    }
                }).create();
                dialog.show();
                break;
        }
        return true;
    }
}
