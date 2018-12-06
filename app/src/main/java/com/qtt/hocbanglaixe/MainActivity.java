package com.qtt.hocbanglaixe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initWidget();
        new Thread() {
            @Override
            public void run() {
                super.run();
                loadUserData();
            }
        }.run();
    }

    private void loadUserData() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            //comback to login Activity
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initWidget() {
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

    public void onBtnLogoutClick(View view) {
        mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, R.string.logout_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, R.string.logout_failed, Toast.LENGTH_SHORT).show();
            }
        });
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
        switch (currentMenu) {
            case MENU_LOGOUT:
                onBtnLogoutClick(null);
                break;
        }
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
