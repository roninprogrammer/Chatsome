package com.dananaka.chatsome.app_intro;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.dananaka.chatsome.firstlogin.UserFirstLoginActivity;

public class IntroActivity extends AppIntro2{

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setGoBackLock(true);
        showSkipButton(false);

        addSlide(new OneFragment());
        addSlide(new TwoFragment());
        addSlide(new ThreeFragment());

//        mInterstitialAd = new InterstitialAd(this);
//        String adUnitId = “[YOUR_INTERSTITIAL_AD_UNIT_ID]”;
//        mInterstitialAd.setAdUnitId(adUnitId);
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        //Finish this activity
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                Intent intent = new Intent(IntroActivity.this,UserFirstLoginActivity.class);
                startActivity(intent);
                finish();
            }

        });


    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        //do the rest
    }

}
