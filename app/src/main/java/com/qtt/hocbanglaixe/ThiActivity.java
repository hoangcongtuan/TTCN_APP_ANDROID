package com.qtt.hocbanglaixe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qtt.hocbanglaixe.Utils.HttpHandler;
import com.qtt.hocbanglaixe.model.Question;
import com.qtt.hocbanglaixe.widget.ProgressDialogBuilderCustom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThiActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String API_URL = "https://hoclaixe-ttcn.herokuapp.com/questions";
    private static final String TAG = ThiActivity.class.getName();
    CountDownTimer timer;
    TextView tvTimer;
    ImageView imgBack;
    private RecyclerView mRcAnswer;
    private LinearLayoutManager mLinearLayoutManager;
    private QuestionAdapter mAdapter;
    private FragmentTransaction transaction;
    private ArrayList<Question> mQuestions = new ArrayList<>();

    @BindView(R.id.tv_num_ques)
    TextView tvNumQues;
    @BindView(R.id.tv_num_ques_below)
    TextView tvNumQuesBelow;

    AlertDialog loadQuestionDialog;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thi);
        ButterKnife.bind(this);
        init();
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
        mAdapter = new QuestionAdapter(this, mQuestions);
        mRcAnswer.setAdapter(mAdapter);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRcAnswer);
        mRcAnswer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(mLinearLayoutManager);
                    int pos = mLinearLayoutManager.getPosition(centerView);
                    tvNumQues.setText(++pos + "");
                    tvNumQuesBelow.setText(pos + "");
                    Log.e("Snapped Item Position:",""+pos);
                }
            }
        });
        imgBack.setOnClickListener(this);
    }

    private void init() {
        GetQuestionAsyncTask asyncTask = new GetQuestionAsyncTask();
        asyncTask.execute(API_URL);
    }

    class GetQuestionAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialogBuilderCustom builderCustom = new ProgressDialogBuilderCustom(ThiActivity.this);
            builderCustom.setText(R.string.loading);
            loadQuestionDialog = builderCustom.create();

            loadQuestionDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();

            String url = strings[0];
            String jsonString = httpHandler.makeServiceCall(url);
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadQuestionDialog.dismiss();
            prepareQuestion(s);
            Log.d(TAG, "onPostExecute: " + s);
        }
    }

    private void prepareQuestion(String jsonQuestion) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<Question>>(){}.getType();
        List<Question> questions = gson.fromJson(jsonQuestion, collectionType);
        mAdapter.setQuestionList(questions);
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "prepareQuestion: ");
    }


//    private void loadData() {
//        mQuestions.add(new Answer());
//        mQuestions.add(new Answer());
//        mQuestions.add(new Answer());
//        mQuestions.add(new Answer());
//        mQuestions.add(new Answer());
//        mAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
