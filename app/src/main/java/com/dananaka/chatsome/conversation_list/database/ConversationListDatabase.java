package com.dananaka.chatsome.conversation_list.database;

import com.dananaka.chatsome.user.data_model.User;

import java.util.List;

import rx.Observable;

/**
 * Created by Vicknesh on 29/12/16.
 */

public interface ConversationListDatabase {

    Observable<List<String>> observeConversationsFor(User user);

    Observable<Integer> observeUnreadCount(String self, String destination);

    void setUnreadCount(String self, String destination, Integer value);

}
