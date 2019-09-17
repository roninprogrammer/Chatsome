package com.dananaka.chatsome.about.view;

import android.graphics.Bitmap;
import android.widget.TextView;

import com.dananaka.chatsome.user.data_model.User;

/**
 * Created by Vicknesh on 22/02/17.
 */

public interface AboutDisplayer {

    void aboutUs();

    void attach(AboutActionListener AboutActionListener);

    void detach(AboutActionListener AboutActionListener);

    interface AboutActionListener {

        void onUpPressed();

        void onOkPressed();

    }

}
