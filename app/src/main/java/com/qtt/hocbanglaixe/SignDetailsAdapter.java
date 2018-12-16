package com.qtt.hocbanglaixe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtt.hocbanglaixe.model.SignDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignDetailsAdapter extends RecyclerView.Adapter<SignDetailsAdapter.SignDetailViewHolder> {
    Context mContext;
    List<SignDetail> signDetailList;
    SignDetailAdapterListener mListener;

    public SignDetailsAdapter(Context context) {
        this.mContext = context;
        signDetailList = new ArrayList<>();
    }

    public void setSignList(List<SignDetail> list) {
        signDetailList.clear();
        signDetailList.addAll(list);
    }

    public void setListener(SignDetailAdapterListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public SignDetailsAdapter.SignDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign_detail, parent, false);
        return new SignDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SignDetailsAdapter.SignDetailViewHolder holder, int position) {
        SignDetail detail = signDetailList.get(position);
        holder.tvTitle.setText(detail.getNAME());
        holder.tvDescription.setText(detail.getDESC());
        mListener.requestImage(detail.getIMAGE(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return signDetailList.size();
    }

    class SignDetailViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        public SignDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface SignDetailAdapterListener {
        void requestImage(String imgID, ImageView imageView);
    }
}
