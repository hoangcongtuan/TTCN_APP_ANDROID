package com.qtt.hocbanglaixe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
/**
 * Created by Admin on 3/22/2017.
 */

public abstract class BaseFragment extends Fragment {
    protected View mView;
    protected int mViewId;
    protected Context mContext;
    protected ProgressDialog mProgressDialog;

    protected abstract int initLayout();

    protected abstract void initComponents();

    protected abstract void addListener();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = initLayout();
        if (layoutId != 0) {
            mViewId = layoutId;
        }
        mView = LayoutInflater.from(getActivity()).inflate(mViewId, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Vui lòng đợi");
        initComponents();
        addListener();
    }

    public void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void toast(int messageId) {
        Toast.makeText(mContext, getString(messageId), Toast.LENGTH_SHORT).show();
    }

    public void showLoading(boolean isShow) {
        if (isShow) {
            mProgressDialog.show();
        } else {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }



    protected void hideKeyBoard() {
        try {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        InputMethodManager inputManager = (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getApplicationWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (IllegalStateException e) {
                    } catch (Exception e) {
                    }
                }
            });

        } catch (IllegalStateException e) {
            // TODO: handle exception
        } catch (Exception e) {
        }
    }
}
