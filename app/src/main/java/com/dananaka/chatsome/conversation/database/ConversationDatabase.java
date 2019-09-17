package com.dananaka.chatsome.conversation.database;

import com.google.firebase.database.DatabaseReference;
import com.dananaka.chatsome.conversation.data_model.Chat;
import com.dananaka.chatsome.conversation.data_model.Message;

import rx.Observable;

/**
 * Created by Vicknesh on 08/02/17.
 */

public interface ConversationDatabase {

    Observable<Message> observeAddMessage(String self, String destination);

    Observable<Message> observeLastMessage(String self, String destination);

    Observable<Chat> observeChat(String self, String destination);

    void sendMessage(String user, Message message);

    Observable<Boolean> observeTyping(String self, String destination);

    void setTyping(String self, String destination, Boolean value);

    Observable<Integer> observeUnreadCount(String self, String destination);

    void setUnreadCount(String self, String destination, Integer value);

    void setMessageEnabled(String self, String destination, Boolean value);

}
