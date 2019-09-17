package com.dananaka.chatsome.main.service;

import com.dananaka.chatsome.user.data_model.User;

/**
 * Created by Vicknesh on 16/12/16.
 */

public interface MainService {

    String getLoginProvider() throws Exception;

    void initLastSeen(User user);

    void logout();

}
