package com.szubov.android_hw_121;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExternalFile {

    private Activity mActivity;
    private String mFileName;
    private static final String LOG_TAG = "My app";


    public ExternalFile(Activity mActivity, String mFileName) {
        this.mActivity = mActivity;
        this.mFileName = mFileName;
    }

    public void saveItemsToFile(ItemData itemData, int index) {
        if (isExternalStorageWritable()) {
            try {
                File file = new File(mActivity.getExternalFilesDir(null),mFileName);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                bw.append(String.valueOf(index)).append(",").
                        append(itemData.getTitle()).append(",").append(itemData.getSubtitle()).append("\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(LOG_TAG, "External storage not available");
        }
    }

    public List<String> loadItemsFromFile() {
        List<String> list = new ArrayList<>();

        if (isExternalStorageWritable()) {
            File file = new File(mActivity.getExternalFilesDir(null),mFileName);
            if (file.length() > 0) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String string;
                    while ((string = br.readLine()) != null) {
                        list.add(string);
                    }
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return list;
    }

    public void removeItemFromFile(ItemData itemData) {
        if (isExternalStorageWritable()) {
            File file = new File(mActivity.getExternalFilesDir(null), mFileName);
            List<String> list = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String oldString;
                while ((oldString = br.readLine()) != null) {
                    if (!oldString.contains(itemData.getTitle())) {
                        list.add(oldString);
                    }
                }
                br.close();
                /*FileWriter writer = new FileWriter(mFileName,false);
                writer.write(sb.toString());

                writer.flush();
                writer.close();*/
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                for (String string :list) {
                    bw.append(string).append("\n");
                }
                bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            Log.e(LOG_TAG, "External storage not available");
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
