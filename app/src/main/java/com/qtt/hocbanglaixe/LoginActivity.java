package com.qtt.hocbanglaixe;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.qtt.hocbanglaixe.widget.ProgressDialogBuilderCustom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGIN_IN = 1;
    private static final String TAG = LoginActivity.class.getName();
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.progresBar_login)
    ProgressBar progressBarLogin;
    @BindView(R.id.viewgroup_login)
    ConstraintLayout layout_login_button;
    @BindView(R.id.root_layout)
    CoordinatorLayout root_layout;

    AlertDialog progressDialog;

    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
        initWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLogin();
    }

    private void initWidget() {
        progressBarLogin.setVisibility(View.INVISIBLE);
        tvUserName.setVisibility(View.INVISIBLE);
    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ProgressDialogBuilderCustom progressDialogBuilderCustom = new ProgressDialogBuilderCustom(this);
        progressDialogBuilderCustom.setText(R.string.processing);
        progressDialog = progressDialogBuilderCustom.create();
    }


    private void checkLogin() {
        GoogleSignInAccount currentAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (currentAccount != null)
            continueWith(currentAccount);
    }

    private void requestSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGIN_IN);
    }

    private void continueWith(GoogleSignInAccount currentAccount) {
        tvUserName.setText(currentAccount.getDisplayName());
        tvUserName.setVisibility(View.VISIBLE);
        progressBarLogin.setVisibility(View.VISIBLE);
        layout_login_button.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        GoogleSignInAccount account;
        try {
            account = task.getResult(ApiException.class);
            progressDialog.dismiss();
            continueWith(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Snackbar.make(root_layout, R.string.login_gg_failed, Snackbar.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.btnLoginGg)
    public void onBtnGoogleSignInClick(View view) {
        progressDialog.show();
        requestSignIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGIN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
        }
    }
}
