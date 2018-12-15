package com.qtt.hocbanglaixe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qtt.hocbanglaixe.model.Question;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private Context mContext;
    private List<Question> mData;
    private IOnItemClickedListener mIOnItemClickedListener;

    public void setOnItemClickListener(IOnItemClickedListener listener) {
        mIOnItemClickedListener = listener;
    }

    public QuestionAdapter(Context context, List<Question> Answer) {
        this.mContext = context;
        this.mData = Answer;
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
                    if (mIOnItemClickedListener != null) {
                        mIOnItemClickedListener.onClick(getLayoutPosition());
                    }
                }
            });

        }

    }

    public interface IOnItemClickedListener {
        void onClick(int positon);
    }

}

