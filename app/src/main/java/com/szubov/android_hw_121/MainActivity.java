package com.szubov.android_hw_121;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random mRandom = new Random();
    private ItemsDataAdapter mAdapter;
    private List<Drawable> mImages = new ArrayList<>();
    private static final String LOG_TAG = "My app";
    private File mListSamples;
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        fillImages();

        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            generateRandomData();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }

        FloatingActionButton mFab = findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomData();
            }
        });

        ListView mListView = findViewById(R.id.listView);
        mAdapter = new ItemsDataAdapter(this, null);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showItemData(position);
                return true;
            }
        });
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    generateRandomData();
                } else {
                    Toast.makeText(this, R.string.toast_app_need_write_permission,
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    /*private void toDo() {
        if (isExternalStorageWritable()) {
            mListSamples = new File(getPrivateAlbumStorageDir(this, "Files"),
                    "listSamples.txt");
        }
    }*/

    /*private File getPrivateAlbumStorageDir(Context context) {
        File file = new File(getApplicationContext().getExternalFilesDir(null));
        if (!file.mkdirs()) {
            Log.e(LOG_TAG,"Directory not created");
        }
        return file;
    }*/

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void generateRandomData() {
        if (isExternalStorageWritable()) {
            mListSamples = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "listSamples.txt");
        }

        FileWriter mItemWriter = null;

        try {
            mItemWriter = new FileWriter(mListSamples, true);
            mItemWriter.append(String.valueOf(mRandom.nextInt(mImages.size()))).
                    append(",").append(getString(R.string.text_view_title)).
                    append(String.valueOf(mAdapter.getCount())).append(",").
                    append(getString(R.string.text_view_subtitle)).append(";");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mItemWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileReader mItemReader = null;

        try {
            mItemReader = new FileReader(mListSamples);
            String[] items = mItemReader.toString().split(";");
            for (String item : items) {
                String[] strings = item.split(",");
                mAdapter.addItem(new ItemData(mImages.get(Integer.parseInt(strings[0])), strings[1], strings[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mItemReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void showItemData(int position) {
        ItemData itemData = mAdapter.getItem(position);
        Toast.makeText(MainActivity.this,getString(R.string.toast_title) +
                itemData.getTitle() + "\n" + getString(R.string.toast_subtitle) +
                itemData.getSubtitle(), Toast.LENGTH_LONG).show();
    }

    private void fillImages() {
        mImages.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_compass));
        mImages.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_day));
        mImages.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_agenda));
        mImages.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_camera));
        mImages.add(ContextCompat.getDrawable(MainActivity.this,
                android.R.drawable.ic_menu_call));
    }
}