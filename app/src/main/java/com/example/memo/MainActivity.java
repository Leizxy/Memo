package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private EditText search;
    private RecyclerView rv;
    private TextView add;
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private MemoAdapter memoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("待办事项");
        requestPermissions();
        search = findViewById(R.id.search);
        rv = findViewById(R.id.rv);
        add = findViewById(R.id.add);

        initSearch();
        add.setOnClickListener(view -> {
            startActivity(new Intent(this, AddActivity.class));
        });
        memoAdapter = new MemoAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(memoAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    private void initSearch() {
        search.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                Log.i("MainActivity", "onEditorAction: " + search.getText().toString());
                memoAdapter.setData(App.getInstance().getHelper().getMemo(search.getText().toString().trim()));
            }
            return false;
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    initList();
                }
            }
        });
    }

    private void initList() {
        List<MemoBean> todoMemos = App.getInstance().getHelper().getTodoMemos(System.currentTimeMillis());
        List<MemoBean> expiredMemos = App.getInstance().getHelper().getExpiredMemos(System.currentTimeMillis());
        todoMemos.addAll(expiredMemos);
        memoAdapter.setData(todoMemos);
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

    private void requestPermissions() {
        Log.i("MainActivity", "requestPermissions: ");
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    private boolean checkPermission() {
        for (String per : permissions) {
            if (ContextCompat.checkSelfPermission(this, per) != PERMISSION_GRANTED) {
                Log.i("MainActivity", "checkPermission: " + per);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            requestPermissions();
        }
    }
}