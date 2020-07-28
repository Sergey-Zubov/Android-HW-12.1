package com.szubov.android_hw_121;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
    private List<Drawable> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        fillImages();

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

    private void generateRandomData() {
        mAdapter.addItem(new ItemData(mImages.get(mRandom.nextInt(mImages.size())),
                getString(R.string.text_view_title) + mAdapter.getCount(),
                getString(R.string.text_view_subtitle)));
    }

    private void showItemData(int position) {
        ItemData itemData = mAdapter.getItem(position);
        Toast.makeText(MainActivity.this,getString(R.string.toast_title) +
                itemData.getTitle() + "\n" + getString(R.string.toast_subtitle) +
                itemData.getSubtitle(), Toast.LENGTH_LONG).show();
    }
}