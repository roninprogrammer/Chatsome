package com.dananaka.chatsome.user.database;

import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.data_model.Users;

import rx.Observable;

/**
 * Created by Vicknesh on 27/12/16.
 */

public interface UserDatabase {

    Observable<Users> observeUsers();

    Observable<Users> observeOnlineUsers();

    Observable<User> readUserFrom(String userId);

    Observable<Users> singleObserveUsers();

    Observable<User> observeUser(String userId);

    Observable<Boolean>  initUserLastSeen();

    void setUserLastSeen(String userId);

    void setUserLastSeenAfterLogout(String userId);

    void setUserName(String userId, String name);

    void setUserStatus(String userId, String status);

    void setUserPlaceName(String userId, String placeName);

    void setUserPlaceLat(String userId, String placeLat);

    void setUserPlaceLong(String userId, String placeLong);

    void setUserImage(String userId, String image);

}
