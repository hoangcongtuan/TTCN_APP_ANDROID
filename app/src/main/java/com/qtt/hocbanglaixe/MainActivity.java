package com.qtt.hocbanglaixe;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener,  MenuItemAdapter.IOnMenuItemClicklistener {
    private DrawerLayout drawer;
    protected Fragment mFragment;
    private TextView mTvTitle;
    private MENU_ITEM mCurrentMenu, mMenuBefore;
    public enum MENU_ITEM {MENU_LEARN, MENU_LOGOUT, MENU_SETTING, MENU_SUPPORT}
    private DrawerLayout mDrawerLayout;
    private View mLayoutSlideMenu, mCurrentTab, mTabContact, mTabAccount;
    private RecyclerView mRecyclerViewMenu;
    private TextView mTvFullname;
    private ImageView mImvAvatar, mImvBack;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.getSupportActionBar().hide();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mLayoutSlideMenu = findViewById(R.id.layout_left_menu);
        mRecyclerViewMenu = findViewById(R.id.recyclerview_menu);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mRecyclerViewMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(MENU_ITEM.MENU_LEARN, R.drawable.ic_logout, "Học bằng lái xe"));
        menuItems.add(new MenuItem(MENU_ITEM.MENU_SUPPORT, R.drawable.ic_logout, "Hướng dẫn"));
        menuItems.add(new MenuItem(MENU_ITEM.MENU_SETTING, R.drawable.ic_logout, "Cài đặt"));
        menuItems.add(new MenuItem(MENU_ITEM.MENU_LOGOUT, R.drawable.ic_logout, "Đăng xuất"));
        MenuItemAdapter menuAdapter = new MenuItemAdapter(this, menuItems);
        menuAdapter.setItemListener(this);
        mRecyclerViewMenu.setAdapter(menuAdapter);

        mCurrentMenu = MENU_ITEM.MENU_LEARN;
        menuAdapter.setItemSelected(MENU_ITEM.MENU_LEARN);
        mDrawerLayout.addDrawerListener(this);

        menuAdapter.setItemSelected(MENU_ITEM.MENU_LEARN);
        setTitle("Học bằng lái xe");
        setNewPage(new LearnFragment());


    }
    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onItemClick(MENU_ITEM menuId, MENU_ITEM currentMenu) {

    }
    public void setNewPage(Fragment fragment) {
        try {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_main, fragment, "currentFragment");
            transaction.commitAllowingStateLoss();
            if (mFragment != null)
                transaction.remove(mFragment);
            mFragment = fragment;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }



}
