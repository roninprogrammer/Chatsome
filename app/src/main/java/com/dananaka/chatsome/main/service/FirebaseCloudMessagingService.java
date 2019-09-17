package com.dananaka.chatsome.main.service;

import com.dananaka.chatsome.main.database.CloudMessagingDatabase;
import com.dananaka.chatsome.user.data_model.User;

import rx.Observable;

/**
 * Created by Vicknesh on 17/12/16.
 */

public class FirebaseCloudMessagingService implements CloudMessagingService {

    private CloudMessagingDatabase messagingDatabase;

    public FirebaseCloudMessagingService(CloudMessagingDatabase messagingDatabase) {
        this.messagingDatabase = messagingDatabase;
    }

    @Override
    public Observable<String> readToken(User user) {
        return messagingDatabase.readToken(user);
    }

    @Override
    public void setToken(User user) {
        messagingDatabase.setToken(user);
    }
}
