package com.dananaka.chatsome.user.view;

import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.data_model.Users;

/**
 * Created by Vicknesh on 01/02/17.
 */

public interface UsersDisplayer {

    void display(Users users);

    void attach(UserInteractionListener userInteractionListener);

    void detach(UserInteractionListener userInteractionListener);

    interface UserInteractionListener {

        void onUserSelected(User user);

    }

    void filter(String text);

}
