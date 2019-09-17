package com.dananaka.chatsome.profile.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.dananaka.chatsome.user.data_model.User;

/**
 * Created by Vicknesh on 14/01/17.
 */

public interface ProfileDisplayer {

    void display(User user);

    void updateProfileImage(Bitmap bitmap);

    void attach(ProfileActionListener profileActionListener);

    void detach(ProfileActionListener profileActionListener);

    interface ProfileActionListener {

        void onUpPressed();

        void onNamePressed(String hint, TextView textView);

        void onStatusPressed(String hint, TextView textView);

        void onPasswordPressed(String hint);

        void onImagePressed();

        void onSavePressed();

        void onRemovePressed();

    }

}
