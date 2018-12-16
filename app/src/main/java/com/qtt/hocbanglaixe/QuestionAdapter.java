package com.qtt.hocbanglaixe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtt.hocbanglaixe.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private Context mContext;
    private List<Question> mData;
    private QuestionAdapterListener mListener;

    private boolean[][] answers = new boolean[20][4];

    public void setOnItemClickListener(QuestionAdapterListener listener) {
        mListener = listener;
    }

    public QuestionAdapter(Context context, List<Question> Answer) {
        this.mContext = context;
        this.mData = Answer;
        for(int i = 0; i < 20; i++)
            Arrays.fill(this.answers[i], false);
    }

    public void setQuestionList(List<Question> questions) {
        this.mData.clear();
        this.mData.addAll(questions);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question item = mData.get(position);

        holder.tvQuestion.setText(item.getQUESTIONCONTENT());

        if (item.getIMAGE() != null) {
            mListener.requestImage(item.getIMAGE(), holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
        } else
            holder.imageView.setVisibility(View.GONE);

        String ans1 = item.getOPTION1();
        String ans2 = item.getOPTION2();
        String ans3 = item.getOPTION3();
        String ans4 = item.getOPTION4();


        if (ans1 != null) {
            holder.layout_answer1.setVisibility(View.VISIBLE);
            holder.tvAnswer1.setText(ans1);
        }
        else
            holder.layout_answer1.setVisibility(View.GONE);

        if (ans2 != null) {
            holder.layout_answer2.setVisibility(View.VISIBLE);
            holder.tvAnswer2.setText(ans2);
        }
        else
            holder.layout_answer2.setVisibility(View.GONE);

        if (ans3 != null) {
            holder.layout_answer3.setVisibility(View.VISIBLE);
            holder.tvAnswer3.setText(ans3);
        }
        else
            holder.layout_answer3.setVisibility(View.GONE);

        if (ans4 != null) {
            holder.layout_answer4.setVisibility(View.VISIBLE);
            holder.tvAnswer4.setText(ans4);
        }
        else
            holder.layout_answer4.setVisibility(View.GONE);

        boolean[] answerTable = answers[position];
        int selectColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
        int noneSelectColor = Color.TRANSPARENT;

        holder.layout_answer1.setBackgroundColor(answerTable[0]?selectColor:noneSelectColor);
        holder.layout_answer2.setBackgroundColor(answerTable[1]?selectColor:noneSelectColor);
        holder.layout_answer3.setBackgroundColor(answerTable[2]?selectColor:noneSelectColor);
        holder.layout_answer4.setBackgroundColor(answerTable[3]?selectColor:noneSelectColor);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvQuestion)
        TextView tvQuestion;
        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.layout_answer1)
        LinearLayout layout_answer1;
        @BindView(R.id.layout_answer2)
        LinearLayout layout_answer2;
        @BindView(R.id.layout_answer3)
        LinearLayout layout_answer3;
        @BindView(R.id.layout_answer4)
        LinearLayout layout_answer4;

        @BindView(R.id.tvAnswer1)
        TextView tvAnswer1;
        @BindView(R.id.tvAnswer2)
        TextView tvAnswer2;
        @BindView(R.id.tvAnswer3)
        TextView tvAnswer3;
        @BindView(R.id.tvAnswer4)
        TextView tvAnswer4;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onQuestionClick(getLayoutPosition());
                    }
                }
            });

        }

        @OnClick(R.id.layout_answer1)
        public void onLayoutAns1Click(View view) {
            toggleAnswer(getAdapterPosition(), 0, layout_answer1);
        }

        @OnClick(R.id.layout_answer2)
        public void onLayoutAns2Click(View view) {
            toggleAnswer(getAdapterPosition(), 1, layout_answer2);
        }

        @OnClick(R.id.layout_answer3)
        public void onLayoutAns3Click(View view) {
            toggleAnswer(getAdapterPosition(), 2, layout_answer3);
        }

        @OnClick(R.id.layout_answer4)
        public void onLayoutAns4Click(View view) {
            toggleAnswer(getAdapterPosition(), 3, layout_answer4);
        }
    }

    private void toggleAnswer(int questionId, int answer, View layout_answer) {
        boolean isSelect;
        isSelect = ((answers[questionId][answer] = !answers[questionId][answer]));
        if (isSelect) {
            layout_answer.setBackgroundColor(ContextCompat.getColor(layout_answer.getContext(), R.color.colorPrimary));
        } else
            layout_answer.setBackgroundColor(Color.TRANSPARENT);
    }

    public interface QuestionAdapterListener {
        void onQuestionClick(int positon);
        void requestImage(String imageID, ImageView imageView);
    }

}

