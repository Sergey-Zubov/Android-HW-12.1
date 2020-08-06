package com.szubov.android_hw_121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random mRandom = new Random();
    private ItemsDataAdapter mAdapter;
    private ExternalFile mExternalFile = null;
    private List<Drawable> mImages = new ArrayList<>();
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

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
        }

        FloatingActionButton mFab = findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfImage = mRandom.nextInt(mImages.size());
                ItemData itemData = generateRandomData(indexOfImage);
                mAdapter.addItem(itemData);
                mExternalFile.saveItemsToFile(itemData, indexOfImage);
            }
        });

        ListView mListView = findViewById(R.id.listView);
        mExternalFile = new ExternalFile(MainActivity.this, "Items.txt");
        mAdapter = new ItemsDataAdapter(this, null, mExternalFile);

        mListView.setAdapter(mAdapter);

        loadItems();

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showItemData(position);
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.toast_app_need_write_permission,
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void loadItems() {
        List<String> list = mExternalFile.loadItemsFromFile();
        if (list != null) {
            for (String item : list) {
                String[] strings = item.split(",");
                mAdapter.addItem(new ItemData(mImages.get(Integer.parseInt(strings[0])), strings[1], strings[2]));
            }
        }
    }

    private ItemData generateRandomData(int index) {
        return new ItemData(mImages.get(index),
                getString(R.string.text_view_title) + mAdapter.getCount(),
                getString(R.string.text_view_subtitle));
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