package com.szubov.android_hw_121;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExternalFile {

    private Activity mActivity;
    private File mListSamples;
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 12;
    private static final String LOG_TAG = "My app";


    public ExternalFile(Activity mActivity) {
        this.mActivity = mActivity;

        int permissionStatus = ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            if (!mActivity.getApplicationContext().
                    getExternalFilesDir(null).getPath().contains("Items.txt")) {
                Log.e(LOG_TAG,"Directory not created");
                addDirectory();
            }
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);

        }
    }

    public void saveItemsToFile(ItemData itemData) {
        FileWriter mItemWriter = null;

        try {
            mItemWriter = new FileWriter(mListSamples, true);
            mItemWriter.append(String.valueOf(itemData.getImage())).append(",").
                    append(itemData.getTitle()).append(",").append(itemData.getSubtitle()).append(";");
            /*ItemData itemData = new ItemData(mImages.get(mRandom.nextInt(mImages.size())),
                    getString(R.string.text_view_title) + mAdapter.getCount(),
                    getString(R.string.text_view_subtitle));
            mAdapter.addItem(itemData);
            mItemWriter = new FileWriter(mListSamples, true);
            mItemWriter.append(itemData.getImage()).append(",").
                    append(itemData.getTitle()).append(String.valueOf(mAdapter.getCount())).
                    append(",").append(itemData.getSubtitle()).append(";");*/
            /*mItemWriter.append(mAdapter.addItem(new ItemData(mImages.get(mRandom.nextInt(mImages.size())),
                    getString(R.string.text_view_title) + mAdapter.getCount(),
                    getString(R.string.text_view_subtitle))));*/
            /*mItemWriter.append(String.valueOf(mRandom.nextInt(mImages.size()))).
                    append(",").append(getString(R.string.text_view_title)).
                    append(String.valueOf(mAdapter.getCount())).append(",").
                    append(getString(R.string.text_view_subtitle)).append(";");*/
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mItemWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ItemData> loadItemsFromFile(List<Drawable> images) {
        FileReader mItemReader = null;
        List<ItemData> itemData = new ArrayList<>();

        if (mListSamples != null && mListSamples.length() > 0) {
            try {
                mItemReader = new FileReader(mListSamples);
                String[] items = mItemReader.toString().split(";");
                for (String item : items) {
                    String[] value = item.split(",");
                    itemData.add(new ItemData(images.get(Integer.parseInt(value[0])), value[1], value[2]));
                    /*String[] strings = item.split(",");
                    mAdapter.addItem(new ItemData(mImages.get(Integer.parseInt(strings[0])), strings[1], strings[2]));*/

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert mItemReader != null;
                    mItemReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return itemData;
    }

    public void removeItemFromFile(int position) {

    }

    public void addDirectory() {
        if (isExternalStorageWritable()) {
            mListSamples = new File(mActivity.getApplicationContext().getExternalFilesDir(null), "Items.txt");
        } else {
            Log.e(LOG_TAG, "External storage not available");
            Toast.makeText(mActivity, R.string.external_storage_not_available, Toast.LENGTH_LONG).show();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}