package com.dananaka.chatsome.navigation;

import android.app.Activity;
import android.content.Intent;


import com.dananaka.chatsome.login.LoginActivity;
import com.dananaka.chatsome.main.MainActivity;

/**
 * Created by Vicknesh on 27/12/16.
 */

public class AndroidNavigator implements Navigator {

    private final Activity activity;

    public AndroidNavigator(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void toLogin() {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void toMainActivity() {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();

    }

    @Override
    public void toParent() {
        activity.finish();
    }

}
