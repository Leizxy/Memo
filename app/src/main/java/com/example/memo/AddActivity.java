package com.example.memo;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @date 12/8/20
 * @description
 */
public class AddActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 101;
    public static final int PICK_PHOTO = 102;
    private GridImgAdapter adapter;
    private AppCompatSpinner spinner;
    private int currentCategory = -1;
    private List<MemoTypeBean> categories;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private long saveTime = -1;
    private boolean modify;
    private TextView time;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("添加");
        RecyclerView rv = findViewById(R.id.img_rv);
        EditText title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        spinner = findViewById(R.id.select);
        modify = getIntent().getBooleanExtra("modify", false);
        id = getIntent().getIntExtra("id", -1);
        initTimePicker();

        categories = App.getInstance().getHelper().getCategories();
        initSpinner();
        //图片显示
        rv.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        adapter = new GridImgAdapter(this, () -> {
            Log.i("AddActivity", "onCreate: " + getFilePath());
            ChooseDialog dialog = new ChooseDialog.Builder(this).setClick(new ChooseDialog.OnClick() {
                @Override
                public void clickCamera() {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);   //拍照界面的隐式意图
                    startActivityForResult(intent, TAKE_PHOTO);
                }

                @Override
                public void clickPhoto() {
                    //打开相册
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Intent.ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO); // 打开相册
                }
            }).create();
            dialog.show();
        });
        rv.setAdapter(adapter);
        if (modify) {
            MemoBean memo = App.getInstance().getHelper().getMemo(id);
            if (memo != null) {
                title.setText(memo.getContent());
                saveTime = memo.getTime();
                time.setText(format.format(new Date(saveTime)));
                spinner.setSelection(memo.getCategory(), true);
                for (String str : memo.getImgs().split(",")) {
                    adapter.addImg(str);
                }
            }
        }

        findViewById(R.id.save).setOnClickListener(v -> {
            if (title.getText().length() > 0) {
                if (modify) {
                    App.getInstance().getHelper().addMemo(id, title.getText().toString().trim(), currentCategory, adapter.getImgs(), saveTime);
                } else {
                    App.getInstance().getHelper().addMemo(title.getText().toString().trim(), currentCategory, adapter.getImgs(), saveTime);
                }
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "请输入备忘内容", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initTimePicker() {
        TimePickerView pvTime = new TimePickerBuilder(this, (date, v) -> {
            saveTime = date.getTime();
            Log.i("AddActivity", "onCreate: " + date);
            Log.i("AddActivity", "onCreate: " + saveTime);
            time.setText(getTime(date));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(Calendar.getInstance(), null)
                .build();
        time.setOnClickListener(v -> {
            pvTime.show();
        });
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        return format.format(date);
    }

    private String getFilePath() {
        File file = new File(getSDCardPath() + File.separator + "memo");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getPath();
    }

    private String getSDCardPath() {
        File path = Environment.getExternalStorageDirectory();
        return path.getAbsolutePath();
    }

    //初始化类别选择
    private void initSpinner() {
        categories.add(0, new MemoTypeBean(-1, "未分类"));
        spinner.setPopupBackgroundResource(R.drawable.spinner_item_bg);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("PostActivity", "onItemSelected: " + position);
                currentCategory = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO) {
                handleImage(data);
            } else if (requestCode == TAKE_PHOTO) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap bm = (Bitmap) bundle.get("data");
                    String titleName = System.currentTimeMillis() + "";
                    saveBitmap(bm, titleName);
                    adapter.addImg(getFilePath() + "/" + titleName + ".jpg");
                }
            }
        }
    }

    private void saveBitmap(Bitmap bitmap, String fileName) {
        try {
            String path = getFilePath()
                    + "/" + fileName + ".jpg";
            OutputStream stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            Log.i("BitmapUtil", "saveBitmap: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //处理图片
    private void handleImage(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

        //添加图片并更新
        adapter.addImg(imagePath);

        Log.i("AddActivity", "handleImageOnKitKat: " + imagePath);
    }

    //获取图片地址
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
