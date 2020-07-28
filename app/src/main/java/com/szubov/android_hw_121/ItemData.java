package com.szubov.android_hw_121;

import android.graphics.drawable.Drawable;

public class ItemData {

    private Drawable mImage;
    private String mTitle;
    private String mSubtitle;

    public ItemData(Drawable image, String title, String subtitle) {
        this.mImage = image;
        this.mTitle = title;
        this.mSubtitle = subtitle;
    }

    public Drawable getImage() {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }
}
