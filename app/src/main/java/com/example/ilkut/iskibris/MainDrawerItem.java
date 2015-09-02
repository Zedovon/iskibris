package com.example.ilkut.iskibris;

import android.graphics.drawable.Drawable;

public class MainDrawerItem {

    public String itemTitle;
    public Drawable itemIcon;

    public MainDrawerItem(Drawable itemIcon, String itemTitle) {
        this.itemIcon = itemIcon;
        this.itemTitle = itemTitle;
    }

    public Drawable getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(Drawable itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
