package com.qtt.hocbanglaixe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Admin on 11/14/16.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItem> mData;
    private Context mContext;
    private IOnMenuItemClicklistener mOnClickListener;
    private MainActivity.MENU_ITEM mCurrentMenu;

    public MenuItemAdapter(Context context, List<MenuItem> data) {
        this.mData = data;
        this.mContext = context;
    }

    public void setItemListener(IOnMenuItemClicklistener listener) {
        mOnClickListener = listener;
    }

    public void setItemSelected(MainActivity.MENU_ITEM itemId) {
        this.mCurrentMenu = itemId;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MenuItem menu = mData.get(position);
        holder.tvName.setText(menu.getName());
        holder.imvImage.setImageResource(menu.getResId());
        if (menu.getId() == MainActivity.MENU_ITEM.MENU_LOGOUT) {
            holder.viewDividerTop.setVisibility(View.VISIBLE);
        } else {
            holder.viewDividerTop.setVisibility(View.GONE);
        }
        if (mCurrentMenu != null && mCurrentMenu == menu.getId() && menu.getId() != MainActivity.MENU_ITEM.MENU_LOGOUT) {
            holder.menuGroup.setSelected(true);
        } else {
            holder.menuGroup.setSelected(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).getId() != MainActivity.MENU_ITEM.MENU_LOGOUT) {
                    mCurrentMenu = mData.get(position).getId();
                }
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClick(mCurrentMenu, mData.get(position).getId());
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imvImage;
        private View menuGroup;
        private View viewDividerTop;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_item);
            tvName.setSelected(true);
            imvImage = (ImageView) view.findViewById(R.id.imv_item);
            menuGroup = view.findViewById(R.id.menu_group);
            viewDividerTop = view.findViewById(R.id.view_divider_top);
        }
    }


    public interface IOnMenuItemClicklistener {
        void onItemClick(MainActivity.MENU_ITEM menuId, MainActivity.MENU_ITEM currentMenu);
    }
}
