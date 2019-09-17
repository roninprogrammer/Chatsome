package com.dananaka.chatsome.about.view;

import android.content.Context;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.dananaka.chatsome.BuildConfig;
import com.dananaka.chatsome.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * Created by Vicknesh on 21/02/17.
 */

public class AboutView extends LinearLayout implements AboutDisplayer, View.OnClickListener {

    private Toolbar toolbar;

    private Button aboutButton;
    private TextView versionAbout;
    private  AdView mAdView_about;

    private AboutActionListener actionListener;

    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_about_view, this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        versionAbout = (TextView) findViewById(R.id.about_version);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        mAdView_about = (AdView) findViewById(R.id.adView_about);
    }


    @Override
    public void aboutUs() {

    }

    @Override
    public void attach(AboutActionListener aboutActionListener) {
        this.actionListener = aboutActionListener;
        aboutButton.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(navigationClickListener);

        //set version in about section
        versionAbout.setText("Build Version: "+ BuildConfig.VERSION_NAME);
        //initialize admob
        MobileAds.initialize(getContext().getApplicationContext(), String.valueOf(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView_about.loadAd(adRequest);
    }

    @Override
    public void detach(AboutActionListener profileActionListener) {
        this.actionListener = null;

        aboutButton.setOnClickListener(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutButton:
                actionListener.onOkPressed();
                break;
        }
    }

    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            actionListener.onUpPressed();
        }
    };




}
