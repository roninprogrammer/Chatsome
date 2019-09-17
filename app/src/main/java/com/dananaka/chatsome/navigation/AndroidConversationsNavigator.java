package com.dananaka.chatsome.navigation;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.dananaka.chatsome.conversation.ConversationActivity;


/**
 * Created by Vicknesh on 31/12/16.
 */

public class AndroidConversationsNavigator implements Navigator {

    private final AppCompatActivity activity;
    private final Navigator navigator;

    public AndroidConversationsNavigator(AppCompatActivity activity, Navigator navigator) {
        this.activity = activity;
        this.navigator = navigator;
    }


    @Override
    public void toLogin() {
        activity.onBackPressed();
    }

    @Override
    public void toMainActivity() {

    }

    @Override
    public void toParent() {

    }

    public void toSelectedConversation(Bundle bundle) {
        Intent intent = new Intent(activity, ConversationActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}
