package com.qtt.hocbanglaixe;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ThiActivity extends AppCompatActivity implements View.OnClickListener {
    CountDownTimer timer;
    TextView tvTimer;
    ImageView imgBack;
    private RecyclerView mRcAnswer;
    private LinearLayoutManager mLinearLayoutManager;
    private AnswerAdapter mAdapter;
    private FragmentTransaction transaction;
    private ArrayList<Answer> mData = new ArrayList<>();
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thi);
        tvTimer = findViewById(R.id.tv_timer);
        timer = new CountDownTimer(900000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                ));

            }

            @Override
            public void onFinish() {

            }
        }.start();
        imgBack = findViewById(R.id.img_back);
        mRcAnswer = findViewById(R.id.rc_answer);
        mRcAnswer.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mAdapter = new AnswerAdapter(this, mData);
        mRcAnswer.setAdapter(mAdapter);
        imgBack.setOnClickListener(this);
        loadData();
    }

    private void loadData() {
        mData.add(new Answer());
        mData.add(new Answer());
        mData.add(new Answer());
        mData.add(new Answer());
        mData.add(new Answer());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
