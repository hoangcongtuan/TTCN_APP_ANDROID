package com.qtt.hocbanglaixe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.imageSlider)
    SliderLayout sliderLayout;


    ImageView imgAvatar;
    TextView tvUserName;
    TextView tvEmail;

    GoogleSignInClient mGoogleSignInClient;
    HashMap<String, Integer> image_map = new HashMap<>();
    HashMap<String, String> desc_map = new HashMap<>();


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
        } else {
            tvUserName.setText(account.getDisplayName());
            tvEmail.setText(account.getEmail());
            Glide.with(this).load(account.getPhotoUrl()).into(imgAvatar);
        }

    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        image_map.put("banner_1", R.drawable.banner_1);
        image_map.put("banner_2", R.drawable.banner_2);
        image_map.put("banner_3", R.drawable.banner_3);
        image_map.put("banner_4", R.drawable.banner_4);

        desc_map.put("banner_1", "Ứng dụng thi sát hạch bằng lái xe A1");
        desc_map.put("banner_2", "Ứng dụng thi sát hạch bằng lái A1");
        desc_map.put("banner_3", "Cấu trúc đề thi như thật");
        desc_map.put("banner_4", "Tra cứu hệ thống biển báo");
    }

    private void initWidget() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.main_activity_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imgAvatar);

        for(String key: image_map.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(desc_map.get(key))
                    .image(image_map.get(key))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", key);

            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
    }

    public void onBtnLogoutClick() {
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

    @OnClick(R.id.imgThi)
    void onImgThiClick() {
        Intent intent = new Intent(this, ThiActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imgSign)
    void onImgSignClick() {
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logout:
                onBtnLogoutClick();
                break;
        }
        return false;
    }
}
