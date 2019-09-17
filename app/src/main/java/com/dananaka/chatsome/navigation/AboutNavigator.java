package com.dananaka.chatsome.navigation;

import android.graphics.Bitmap;
import android.widget.TextView;

import com.dananaka.chatsome.user.data_model.User;

/**
 * Created by Vicknesh on 24/02/17.
 */

public interface AboutNavigator extends Navigator {


    void showOkDialog();

    void attach(AboutDialogListener dialogListener);

    void detach(AboutDialogListener dialogListener);

    interface AboutDialogListener {

        void onOkSelected();
    }

}
