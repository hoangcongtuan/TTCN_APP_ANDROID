package com.qtt.hocbanglaixe;

import android.content.Context;
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

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnswerBottomAdapter extends RecyclerView.Adapter<AnswerBottomAdapter.ViewHolder> {
    private Context mContext;
    private List<Boolean> mData;
    private AnswerBottomAdapterListener mListener;
    private int index = -1;
    boolean[] arrAns;
    private boolean checkEnd = false;
    private boolean[][] answers = new boolean[20][4];


    public AnswerBottomAdapter(ThiActivity context, boolean[][] answer, boolean[] arrAns) {
        this.mContext = context;
        this.arrAns = arrAns;
        this.answers = answer;
        checkEnd = true;
    }

    public void setOnItemClickListener(AnswerBottomAdapterListener listener) {
        mListener = listener;
    }

    public AnswerBottomAdapter(Context context, List<Boolean> Answer) {
        this.mContext = context;
        this.mData = Answer;
//        for(int i = 0; i < 20; i++)
//            Arrays.fill(this.answers[i], false);
    }

    public AnswerBottomAdapter(Context context, boolean[][] answers, List<Boolean> Answer) {
        this.mContext = context;
        this.answers = answers;
        this.mData = Answer;
    }

    public void setSeclectIndex(int index) {
        this.index = index;
    }

    @Override
    public AnswerBottomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnswerBottomAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rc_ans, parent, false));
    }

    @Override
    public void onBindViewHolder(AnswerBottomAdapter.ViewHolder holder, int position) {
       // Boolean item = mData.get(position);
        holder.tvNumQues.setText((position + 1) + "");
        boolean[] answerTable = answers[position];
        int selectColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
        int noneSelectColor = Color.TRANSPARENT;
        if (!checkEnd) {
            holder.imgAns.setVisibility(View.GONE);
            if (position + 1 == index) {
                holder.layoutNumQues.setSelected(true);
                holder.tvNumQues.setTextColor(Color.WHITE);
            }
            if (answerTable[0] || answerTable[1] || answerTable[2] || answerTable[3]) {
                holder.tvNumQues.setSelected(true);
            }
        } else {
            holder.imgAns.setVisibility(View.VISIBLE);
            if(arrAns[position]){
                holder.imgAnsWarning.setVisibility(View.GONE);
                holder.imgAns.setVisibility(View.VISIBLE);
            } else {
                holder.imgAnsWarning.setVisibility(View.VISIBLE);
                holder.imgAns.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_num_ques)
        TextView tvNumQues;
        @BindView(R.id.layout_num_ques)
        LinearLayout layoutNumQues;
        @BindView(R.id.img_ans)
        ImageView imgAns;
        @BindView(R.id.img_ans_warning)
        ImageView imgAnsWarning;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onNumQuesClick(getLayoutPosition());
                    }
                }
            });


        }

    }


    public interface AnswerBottomAdapterListener {
        void onNumQuesClick(int positon);
    }

}

