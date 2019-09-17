package com.dananaka.chatsome.login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.facebook.CallbackManager;
import com.dananaka.chatsome.BaseActivity;
import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.login.presenter.LoginPresenter;
import com.dananaka.chatsome.login.view.LoginDisplayer;
import com.dananaka.chatsome.navigation.AndroidLoginNavigator;
import com.dananaka.chatsome.navigation.AndroidNavigator;



/**
 * Created by Vicknesh on 27/01/17.
 */

public class LoginActivity extends BaseActivity {

    private LoginPresenter presenter;
    private AndroidLoginNavigator navigator;
    private CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        LoginDisplayer loginDisplayer = (LoginDisplayer) findViewById(R.id.loginView);
        LoginGoogleApiClient loginGoogleApiClient = new LoginGoogleApiClient(this);
        loginGoogleApiClient.setupGoogleApiClient();

        mCallbackManager = CallbackManager.Factory.create();
        navigator = new AndroidLoginNavigator(this, loginGoogleApiClient, new AndroidNavigator(this));
        presenter = new LoginPresenter(
                Dependencies.INSTANCE.getLoginService(),
                loginDisplayer,
                navigator, mCallbackManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (!navigator.onBackPressed()) {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();

    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

}

