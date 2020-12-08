package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText search;
    private RecyclerView rv;
    private TextView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add.setOnClickListener(view -> {
            startActivity(new Intent(this, AddActivity.class));
        });
        MemoAdapter memoAdapter = new MemoAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(memoAdapter);
    }
}