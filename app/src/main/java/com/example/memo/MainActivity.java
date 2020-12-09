package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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
        setTitle("待办事项");
        search = findViewById(R.id.search);
        rv = findViewById(R.id.rv);
        add = findViewById(R.id.add);
        search.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                Log.i("MainActivity", "onEditorAction: " + search.getText().toString());
            }
            return false;
        });
        add.setOnClickListener(view -> {
            startActivity(new Intent(this, AddActivity.class));
        });
        MemoAdapter memoAdapter = new MemoAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(memoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.to_type:
                startActivity(new Intent(this, TypesActivity.class));
                break;
        }
        return true;
    }
}