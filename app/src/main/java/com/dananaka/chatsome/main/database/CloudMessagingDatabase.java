package com.dananaka.chatsome.main.database;

import com.dananaka.chatsome.user.data_model.User;

import rx.Observable;

/**
 * Created by Vicknesh on 17/12/16.
 */

public interface CloudMessagingDatabase {

    Observable<String> readToken(User user);

    void setToken(User user);

    void enableToken(String userId);

    void disableToken(String userId);

}
