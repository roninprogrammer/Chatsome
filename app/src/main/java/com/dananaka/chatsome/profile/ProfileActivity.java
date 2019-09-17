package com.dananaka.chatsome.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

import com.dananaka.chatsome.BaseActivity;
import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.navigation.AndroidProfileNavigator;
import com.dananaka.chatsome.profile.presenter.ProfilePresenter;
import com.dananaka.chatsome.profile.view.ProfileDisplayer;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vicknesh on 19/12/16.
 */

public class ProfileActivity extends BaseActivity {

    private ProfilePresenter presenter;
    private AndroidProfileNavigator navigator;
    private CircleImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(R.string.profile_toolbar_title);
        ProfileDisplayer profileDisplayer = (ProfileDisplayer) findViewById(R.id.profileView);
        profileImageView = (CircleImageView) findViewById(R.id.profileImageView);

        navigator = new AndroidProfileNavigator(this);
        presenter = new ProfilePresenter(
                Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getProfileService(),
                Dependencies.INSTANCE.getStorageService(),
                profileDisplayer,
                navigator);

        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            profileImageView.setEnabled(false);
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                profileImageView.setEnabled(true);
            }
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