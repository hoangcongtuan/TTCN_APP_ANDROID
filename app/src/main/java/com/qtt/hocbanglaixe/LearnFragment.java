package com.qtt.hocbanglaixe;

import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LearnFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout imgThi, imgHoc, imgTraCuu, imgBienBao, imgCauSai, imgMeoThi;
    @Override
    protected int initLayout() {
        return R.layout.learn_fragment;
    }

    @Override
    protected void initComponents() {
        imgBienBao = mView.findViewById(R.id.img_bienbao);
        imgThi = mView.findViewById(R.id.img_thi);
        imgHoc = mView.findViewById(R.id.img_hoc);
        imgTraCuu = mView.findViewById(R.id.img_tracuu);
        imgCauSai = mView.findViewById(R.id.img_causai);
        imgMeoThi = mView.findViewById(R.id.img_meothi);
    }

    @Override
    protected void addListener() {
        imgThi.setOnClickListener(this);
        imgBienBao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_thi:
                startActivity(new Intent(getContext(), ThiActivity.class));
                break;
            case R.id.img_bienbao:
                startActivity(new Intent(getContext(), SignActivity.class));
                break;
        }
    }
}
