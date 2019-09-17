package com.dananaka.chatsome.navigation;

import android.graphics.Bitmap;
import android.widget.TextView;

import com.dananaka.chatsome.user.data_model.User;

/**
 * Created by Vicknesh on 14/01/17.
 */

public interface ProfileNavigator extends Navigator {

    void showNameTextDialog(String hint, TextView textView, User user);

    void showStatusextDialog(String hint, TextView textView, User user);

    void showInputPasswordDialog(String hint, User user);

    void showImagePicker();

    void showSaveDialog();

    void showRemoveDialog();

    void attach(ProfileDialogListener dialogListener);

    void detach(ProfileDialogListener dialogListener);

    interface ProfileDialogListener {

        void onNameSelected(String text, User user);

        void onStatusSelected(String text, User user);

        void onPasswordSelected(String text);

        void onRemoveSelected();

        void onImageSelected(Bitmap bitmap);

    }

}
