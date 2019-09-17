package com.dananaka.chatsome.main.service;

import com.dananaka.chatsome.user.data_model.User;

import rx.Observable;

/**
 * Created by Vicknesh on 17/12/16.
 */

public interface CloudMessagingService {

    Observable<String> readToken(User user);

    void setToken(User user);

}
