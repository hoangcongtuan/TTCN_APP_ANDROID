package com.qtt.hocbanglaixe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private Context mContext;
    private List<Answer> mData;
    private IOnItemClickedListener mIOnItemClickedListener;

    public void setOnItemClickListener(IOnItemClickedListener listener) {
        mIOnItemClickedListener = listener;
    }

    public AnswerAdapter(Context context, List<Answer> Answer) {
        this.mContext = context;
        this.mData = Answer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Answer item = mData.get(position);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmployeeName;


        public ViewHolder(final View itemView) {
            super(itemView);

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

