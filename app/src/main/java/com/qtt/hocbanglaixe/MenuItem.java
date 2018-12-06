package com.qtt.hocbanglaixe;

/**
 * Created by Admin on 3/22/2017.
 */

public class MenuItem {
    private MainActivity.MENU_ITEM id;
    private int resId;
    private String name;

    public MenuItem() {

    }

    public MenuItem(MainActivity.MENU_ITEM itemId, int resId, String name) {
        this.id = itemId;
        this.resId = resId;
        this.name = name;
    }

    public MainActivity.MENU_ITEM getId() {
        return id;
    }

    public int getResId() {
        return resId;
    }

    public String getName() {
        return name;
    }
}
