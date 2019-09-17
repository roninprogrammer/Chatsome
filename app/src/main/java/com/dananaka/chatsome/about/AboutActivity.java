package com.dananaka.chatsome.about;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.dananaka.chatsome.BaseActivity;
import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.about.presenter.AboutPresenter;
import com.dananaka.chatsome.about.view.AboutDisplayer;
import com.dananaka.chatsome.navigation.AndroidAboutNavigator;

public class AboutActivity extends BaseActivity {
    private AboutPresenter presenter;
    private AndroidAboutNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.about_toolbar_title);
        AboutDisplayer aboutDisplayer = (AboutDisplayer) findViewById(R.id.aboutView);

        navigator = new AndroidAboutNavigator(this);
        presenter = new AboutPresenter(
                Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getStorageService(),
                aboutDisplayer,
                navigator
        );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }

}
