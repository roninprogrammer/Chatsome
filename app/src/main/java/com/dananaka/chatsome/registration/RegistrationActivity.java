package com.dananaka.chatsome.registration;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.dananaka.chatsome.BaseActivity;
import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.navigation.AndroidRegistrationNavigator;
import com.dananaka.chatsome.registration.presenter.RegistrationPresenter;
import com.dananaka.chatsome.registration.view.RegistrationDisplayer;

/**
 * Created by Vicknesh on 28/12/16.
 */

public class RegistrationActivity extends BaseActivity {

    private RegistrationPresenter presenter;
    private AndroidRegistrationNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        RegistrationDisplayer registrationDisplayer = (RegistrationDisplayer) findViewById(R.id.registrationView);
        navigator = new AndroidRegistrationNavigator(this);
        presenter = new RegistrationPresenter(
                Dependencies.INSTANCE.getRegistrationService(),
                registrationDisplayer,
                navigator);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);

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
