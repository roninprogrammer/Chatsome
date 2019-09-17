package com.dananaka.chatsome.profile.view;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.Utils;
import com.dananaka.chatsome.user.data_model.User;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Vicknesh on 14/01/17.
 */

public class ProfileView extends LinearLayout implements ProfileDisplayer, View.OnClickListener {

    private Toolbar toolbar;

    private TextView emailTextView;
    private TextView statusTextView;
//    private PlaceAutocompleteFragment places;
    private TextView nameTextView;
    private TextView passwordTextView;
    private CircleImageView profileImageView;
    private Button saveProfile;
    private Button removeButton;
    private AdView mAdView_profile;

    private ProfileActionListener actionListener;

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_profile_view, this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        emailTextView = (TextView) findViewById(R.id.emailTextView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        profileImageView = (CircleImageView) findViewById(R.id.profileImageView);
        passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        saveProfile = (Button) findViewById(R.id.saveProfile);
        removeButton = (Button) findViewById(R.id.removeButton);
        mAdView_profile = (AdView) findViewById(R.id.adView_profile);
//        places = (PlaceAutocompleteFragment) findFragmentById(R.id.place_autocomplete_fragment);

    }

    @Override
    public void display(User user) {
        emailTextView.setText(user.getEmail());
        nameTextView.setText(user.getName());
        statusTextView.setText(user.getStatus());
        Utils.loadImageElseBlack(user.getImage(),profileImageView,getContext());
    }

    @Override
    public void updateProfileImage(Bitmap bitmap) {
        profileImageView.setImageBitmap(bitmap);
        profileImageView.setDrawingCacheEnabled(true);
        profileImageView.buildDrawingCache();
    }


    @Override
    public void attach(ProfileActionListener profileActionListener) {
        this.actionListener = profileActionListener;
        profileImageView.setOnClickListener(this);
        nameTextView.setOnClickListener(this);
        statusTextView.setOnClickListener(this);
        emailTextView.setOnClickListener(this);
        passwordTextView.setOnClickListener(this);
        saveProfile.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        //initialize admob
        MobileAds.initialize(getContext().getApplicationContext(), String.valueOf(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView_profile.loadAd(adRequest);
    }

    @Override
    public void detach(ProfileActionListener profileActionListener) {
        this.actionListener = null;

        profileImageView.setOnClickListener(null);
        nameTextView.setOnClickListener(null);
        statusTextView.setOnClickListener(null);
        //locationTextView.setOnClickListener(null);
        emailTextView.setOnClickListener(null);
        passwordTextView.setOnClickListener(null);
        saveProfile.setOnClickListener(null);
        removeButton.setOnClickListener(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nameTextView:
                actionListener.onNamePressed("",nameTextView);
                break;
            case R.id.statusTextView:
                actionListener.onStatusPressed("",statusTextView);
                break;
            case R.id.passwordTextView:
                actionListener.onPasswordPressed("");
                break;
            case R.id.profileImageView:
                actionListener.onImagePressed();
                break;
            case R.id.saveProfile:
                actionListener.onSavePressed();
                break;
            case R.id.removeButton:
                actionListener.onRemovePressed();
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
