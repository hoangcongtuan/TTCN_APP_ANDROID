package com.qtt.hocbanglaixe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qtt.hocbanglaixe.Utils.HttpHandler;
import com.qtt.hocbanglaixe.model.Question;
import com.qtt.hocbanglaixe.model.SignDetail;
import com.qtt.hocbanglaixe.widget.ProgressDialogBuilderCustom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSignList extends Fragment implements SignDetailsAdapter.SignDetailAdapterListener {
    private static final String KEY_CATEGORY = "KEY_CATEGORY";

    private static final String GET_IMG_URL = "https://hoclaixe-ttcn.herokuapp.com/images/%s";
    private static final String TAG = FragmentSignList.class.getName();

    @BindView(R.id.recycleView)
    RecyclerView mRecycleView;

    SignDetailsAdapter mAdapter;
    FragmentSignListListener mListener;
    List<SignDetail> mSignDetails;
    HashMap<String, Bitmap> bitmapCache;

    int category;
    boolean isDataLoaded;
    Drawable drawableLoading;

    public static FragmentSignList newInstance(int category) {
        FragmentSignList fragmentSignList = new FragmentSignList();
        Bundle args = new Bundle();
        args.putInt(KEY_CATEGORY, category);
        fragmentSignList.setArguments(args);
        return fragmentSignList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.category = getArguments().getInt(KEY_CATEGORY);
        mSignDetails = new ArrayList<>();
        bitmapCache = new HashMap<>();
        this.isDataLoaded = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup;
        viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_sign_list, container, false);
        if (getUserVisibleHint())
            loadData();
        return  viewGroup;
    }

    private void loadData() {
        if (mListener.isCategoryLoaded(category)) {
            displaySignDetails(mListener.getSignDetails(category));
            isDataLoaded = true;
        } else {
            isDataLoaded = true;
            GetSignDetailAsyncTask asyncTask = new GetSignDetailAsyncTask();
            asyncTask.execute(category);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
        initWidget();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSignListListener)
            this.mListener = (FragmentSignListListener) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isDataLoaded) {
            loadData();
        }
    }

    public void setListener(FragmentSignListListener listener) {
        this.mListener = listener;
    }

    private void init() {
        mAdapter = new SignDetailsAdapter(getContext());
        mAdapter.setSignList(mSignDetails);
        mAdapter.setListener(this);

        drawableLoading = ContextCompat.getDrawable(getContext(), R.drawable.ic_photo_size_select_actual_black_48dp); }

    private void initWidget() {

        LinearLayoutManager llManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        mRecycleView.setLayoutManager(llManager);
        mRecycleView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecycleView.getContext(),
                llManager.getOrientation());
        mRecycleView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void requestImage(final String imgID, final ImageView imageView) {
        imageView.setImageDrawable(drawableLoading);
        if (bitmapCache.containsKey(imgID)) {
            imageView.setImageBitmap(bitmapCache.get(imgID));
            Log.d(TAG, "requestImage: Cached");
        }
        else {
            Log.d(TAG, "requestImage: Downloading...");
            String url = String.format(Locale.US, GET_IMG_URL, imgID);
            Glide.with(getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    bitmapCache.put(imgID, resource);
                    imageView.setImageBitmap(resource);
                    return false;
                }
            }).submit();
        }

    }

    class GetSignDetailAsyncTask extends AsyncTask<Integer, Integer, String> {
        public final static String GET_SIGN_URL = "https://hoclaixe-ttcn.herokuapp.com/sign/%d";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListener.showProgressDialog(true);
        }

        @Override
        protected String doInBackground(Integer... args) {
            HttpHandler httpHandler = new HttpHandler();
            String url = String.format(Locale.US, GET_SIGN_URL, args[0]);
            String jsonString = httpHandler.makeServiceCall(url);
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mListener.showProgressDialog(false);
            displaySignDetails(s);
        }
    }

    private void displaySignDetails(String json) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<SignDetail>>(){}.getType();
        List<SignDetail> signDetails = gson.fromJson(json, collectionType);
        mAdapter.setSignList(signDetails);
        mAdapter.notifyDataSetChanged();

        mListener.cacheSignDetails(category, json);
    }

    public interface FragmentSignListListener {
        List<SignDetail> requestSignList(int category);
        void showProgressDialog(boolean isShow);
        boolean isCategoryLoaded(int category);
        void cacheSignDetails(int category, String json);
        String getSignDetails(int category);
    }

}
