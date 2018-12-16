package com.qtt.hocbanglaixe;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.qtt.hocbanglaixe.model.SignDetail;
import com.qtt.hocbanglaixe.widget.ProgressDialogBuilderCustom;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignActivity extends AppCompatActivity implements FragmentSignList.FragmentSignListListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    SignPagerAdapter mPagerAdapter;
    HashMap<Integer, String> signDetailsCache;

    AlertDialog progressDlg;
    List<FragmentSignList> fragments;
    String[] fragmentTitle = new String[] {
            "Biển báo cấm", "Biển báo hiệu lệnh", "Vạch kẻ đường", "Biển báo phụ",
            "Biển chỉ dẫn", "Biển báo nguy hiểm", "Đường cao tốc", "Tuyến đường đối ngoại"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        init();
        initWidget();
    }

    private void init() {
        signDetailsCache = new HashMap<>();
    }

    private void initWidget() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_sign_activty);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ProgressDialogBuilderCustom builder = new ProgressDialogBuilderCustom(this);
        builder.setText(R.string.processing);
        progressDlg = builder.create();

        mPagerAdapter = new SignPagerAdapter(getSupportFragmentManager());
        for(int i = 9; i < 17; i++) {
            FragmentSignList fragment = FragmentSignList.newInstance(i);
            fragment.setListener(this);
            mPagerAdapter.addFragment(fragment, fragmentTitle[i - 9]);
        }

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(8);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public List<SignDetail> requestSignList(int category) {
        return null;
    }

    @Override
    public void showProgressDialog(boolean isShow) {
        if (isShow)
            progressDlg.show();
        else
            progressDlg.dismiss();
    }

    @Override
    public boolean isCategoryLoaded(int category) {
        return signDetailsCache.containsKey(category);
    }

    @Override
    public void cacheSignDetails(int category, String json) {
        signDetailsCache.put(category, json);
    }

    @Override
    public String getSignDetails(int category) {
        return signDetailsCache.get(category);
    }
}
