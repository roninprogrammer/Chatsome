package com.dananaka.chatsome.conversation.service;

import com.google.firebase.database.DatabaseReference;
import com.dananaka.chatsome.conversation.data_model.Chat;
import com.dananaka.chatsome.conversation.data_model.Message;
import com.dananaka.chatsome.database.DatabaseResult;

import rx.Observable;

/**
 * Created by Vicknesh on 29/12/16.
 */

public interface ConversationService {

    Observable<Message> syncMessages(String self, String destination);

    Observable<DatabaseResult<Chat>> getChat(String self, String destination);

    void sendMessage(String user, Message message);

    Observable<Boolean> getTyping(String self, String destination);

    void setTyping(String self, String destination, Boolean value);

    Observable<Integer> getUnreadCount(String self, String destination);

    void setUnreadCount(String self, String destination, int value);

    void setMessageEnabled(String self, String destination, Boolean value);

}
