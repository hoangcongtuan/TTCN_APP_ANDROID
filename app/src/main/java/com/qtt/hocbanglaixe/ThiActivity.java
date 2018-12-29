package com.qtt.hocbanglaixe;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qtt.hocbanglaixe.Utils.HttpHandler;
import com.qtt.hocbanglaixe.model.Question;
import com.qtt.hocbanglaixe.widget.ProgressDialogBuilderCustom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThiActivity extends AppCompatActivity implements View.OnClickListener, QuestionAdapter.QuestionAdapterListener, AnswerBottomAdapter.AnswerBottomAdapterListener {
    private static final String API_URL = "https://hoclaixe-ttcn.herokuapp.com/questions";
    private static final String TAG = ThiActivity.class.getName();
    private static final String GET_IMG_URL = "https://hoclaixe-ttcn.herokuapp.com/images/%s";
    CountDownTimer timer;
    TextView tvTimer;
    ImageView imgBack;
    private RecyclerView mRcAnswer;
    private RecyclerView mRcAnswerBottom;
    private LinearLayoutManager mLinearLayoutManager;
    private QuestionAdapter mAdapter;
    private AnswerBottomAdapter mBottomAdapter;
    private FragmentTransaction transaction;
    private ArrayList<Question> mQuestions = new ArrayList<>();
    List<Question> questionList;
    private ArrayList<Boolean> mAnsBottom = new ArrayList<>();
    //some flag, variable
    private int currentQuestionIndex; // 0 - 19
    private int prePos = 0;
    private boolean[][] answer;
    private TextView tvEndPractice;
    private boolean[] arrAns;
    private boolean endPracticee = false;

    @BindView(R.id.tv_num_ques)
    TextView tvNumQues;
    @BindView(R.id.tv_num_ques_below)
    TextView tvNumQuesBelow;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

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
                tvTimer.setText(String.format(Locale.US, "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                ));

            }

            @Override
            public void onFinish() {

            }
        }.start();
        tvEndPractice = findViewById(R.id.tv_end);
        tvEndPractice.setOnClickListener(this);
        imgBack = findViewById(R.id.img_back);
        mRcAnswer = findViewById(R.id.rc_answer);
        mRcAnswer.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mAdapter = new QuestionAdapter(this, mQuestions);
        mAdapter.setOnItemClickListener(this);
        mRcAnswer.setAdapter(mAdapter);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRcAnswer);
        currentQuestionIndex = 0;
        mRcAnswer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(mLinearLayoutManager);
                    int pos = mLinearLayoutManager.getPosition(centerView);
                    currentQuestionIndex = pos;
                    tvNumQues.setText(++pos + "");
                    tvNumQuesBelow.setText(pos + "");
//                    for (int i = 0;i< mAdapter.isChoose().size();i++){
//                        if(mAdapter.isChoose().get(i)){
//                            mBottomAdapter.setSeclectIndex(pos);
//                        }
//                    }
//                    for (int i = 0; i < answer.length; i++) {
//                        if (answer[i][0] == true || answer[i][1] == true || answer[i][2] == true || answer[i][3] == true) {
//                            mBottomAdapter.setNumAnsIndex(i);
//                        }
//                    }
                    mBottomAdapter.setSeclectIndex(pos);
                    mBottomAdapter.notifyDataSetChanged();
                    Log.e("Snapped Item Position:", "" + pos);
                }
            }
        });
        imgBack.setOnClickListener(this);
        mRcAnswerBottom = findViewById(R.id.rc_ans);
        mRcAnswerBottom.setLayoutManager(new GridLayoutManager(this, 5));
        for (int i = 0; i < 20; i++) {
            mAnsBottom.add(false);
        }
        mBottomAdapter = new AnswerBottomAdapter(this, mAnsBottom);
        mBottomAdapter.setOnItemClickListener(this);
        mRcAnswerBottom.setAdapter(mBottomAdapter);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        answer = mAdapter.getAnswers();
                        if (!endPracticee) {
                            for (int i = 0; i < 20; i++) {
                                mAnsBottom.add(false);
                            }
                            mBottomAdapter = new AnswerBottomAdapter(ThiActivity.this, answer, mAnsBottom);
                            mBottomAdapter.setSeclectIndex(prePos + 1);
                            mBottomAdapter.setOnItemClickListener(ThiActivity.this);
                            mRcAnswerBottom.setAdapter(mBottomAdapter);
                            mBottomAdapter.notifyDataSetChanged();
                        } else {
                            mBottomAdapter = new AnswerBottomAdapter(ThiActivity.this, answer ,arrAns);
                            mBottomAdapter.setOnItemClickListener(ThiActivity.this);
                            mRcAnswerBottom.setAdapter(mBottomAdapter);
                            mBottomAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void init() {
        GetQuestionAsyncTask asyncTask = new GetQuestionAsyncTask();
        asyncTask.execute(API_URL);
    }

    @Override
    public void onQuestionClick(int positon) {
        Log.d(TAG, "onQuestionClick: " + positon);
    }

    @Override
    public void requestImage(String imageID, ImageView imageView) {
        String imgUrl = String.format(Locale.US, GET_IMG_URL, imageID);
        Glide.with(this).load(imgUrl).into(imageView);
    }

    @Override
    public void onNumQuesClick(int positon) {
        mRcAnswer.smoothScrollToPosition(positon);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        prePos = positon;
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
        Type collectionType = new TypeToken<List<Question>>() {
        }.getType();
        List<Question> questions = gson.fromJson(jsonQuestion, collectionType);
        mQuestions = (ArrayList<Question>) questions;
        mAdapter.setQuestionList(questions);
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "prepareQuestion: ");
    }


    @OnClick(R.id.imgNext)
    void onImgNextClick() {
        if (currentQuestionIndex == 19)
            return;
        mRcAnswer.smoothScrollToPosition(++currentQuestionIndex);

    }

    @OnClick(R.id.imgBack)
    void onImgBackClick() {
        if (currentQuestionIndex == 0)
            return;
        mRcAnswer.smoothScrollToPosition(--currentQuestionIndex);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_end:
                endPractice();
                break;
        }
    }

    private void endPractice() {
        ArrayList<String> arrAnswer = new ArrayList<>();
        boolean[][] booleanAnswer = new boolean[20][4];
        String[] temp;
        for (int i = 0; i < 20; i++) {
            arrAnswer.add(mQuestions.get(i).getANSWERS());
            temp = arrAnswer.get(i).split(",");
            for (int j = 0; j < temp.length; j++) {
                //    intAnswer[i][j] = Integer.parseInt(temp[j]);
//                if(temp[j].length() ==2){
//                    temp[j] = temp[j].split(" ");
//                }
                int a = Integer.parseInt(temp[j]);
                if (a == 1) {
                    booleanAnswer[i][0] = true;
                } else if (a == 2) {
                    booleanAnswer[i][1] = true;
                } else if (a == 3) {
                    booleanAnswer[i][2] = true;
                } else if (a == 4) {
                    booleanAnswer[i][3] = true;
                }
            }
        }
        arrAns = new boolean[20];
        for (int i = 0; i < 20; i++) {
            if ((booleanAnswer[i][0] == answer[i][0]) && (booleanAnswer[i][1] == answer[i][1])
                    & (booleanAnswer[i][2] == answer[i][2]) & (booleanAnswer[i][3] == answer[i][3])) {
                arrAns[i] = true;
            }
        }
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        endPracticee = true;
    }
}
