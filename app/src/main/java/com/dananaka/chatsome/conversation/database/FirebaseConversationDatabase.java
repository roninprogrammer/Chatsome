package com.dananaka.chatsome.conversation.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.dananaka.chatsome.Constants;
import com.dananaka.chatsome.conversation.data_model.Chat;
import com.dananaka.chatsome.conversation.data_model.Message;
import com.dananaka.chatsome.rxrelay.FirebaseObservableListeners;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Vicknesh on 08/02/17.
 */

public class FirebaseConversationDatabase implements ConversationDatabase {

    private static final int DEFAULT_LIMIT = 1000;

    private final DatabaseReference userChat;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseConversationDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        userChat = firebaseDatabase.getReference(Constants.FIREBASE_CHAT);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    private DatabaseReference messagesOfUser(String self, String destination) {
        return userChat.child(self).child(destination).child(Constants.FIREBASE_CHAT_MESSAGES);
    }

    private DatabaseReference unreadCountMsg(String self, String destination) {
        return userChat.child(destination).child(self).child(Constants.FIREBASE_CHAT_UNREADCOUNT);
    }

    @Override
    public Observable<Message> observeAddMessage(String self, String destination) {
        return firebaseObservableListeners.listenToAddChildEvents(messagesOfUser(self,destination), toMessage());
    }

    @Override
    public Observable<Message> observeLastMessage(String self, String destination) {
        return firebaseObservableListeners.listenToSingleValueEvents(messagesOfUser(self,destination).limitToLast(1), toLastMessage());
    }

    @Override
    public Observable<Integer> observeUnreadCount(String self, String destination) {
        return firebaseObservableListeners.listenToValueEvents(unreadCountMsg(self,destination), asInteger());
    }

    @Override
    public Observable<Chat> observeChat(String self, String destination) {
        return firebaseObservableListeners.listenToValueEvents(messagesOfUser(self,destination).limitToLast(DEFAULT_LIMIT), toChat());
    }

    private Func1<DataSnapshot, Chat> toChat() {
        return new Func1<DataSnapshot, Chat>() {
            @Override
            public Chat call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                return new Chat(messages);//.sortedByDate();
            }
        };
    }

    private Func1<DataSnapshot, Message> toLastMessage() {
        return new Func1<DataSnapshot, Message>() {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    messages.add(message);
                }
                return messages.get(0);
            }
        };
    }

    private Func1<DataSnapshot, Message> toMessage() {
        return new Func1<DataSnapshot, Message>() {
            @Override
            public Message call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Message.class);
            }
        };
    }

    @Override
    public void sendMessage(final String user, final Message message) {
        userChat.child(user).child(message.getDestination()).child(Constants.FIREBASE_CHAT_MESSAGES).push().setValue(message);
        userChat.child(message.getDestination()).child(user).child(Constants.FIREBASE_CHAT_MESSAGES).push().setValue(message);
    }

    @Override
    public Observable<Boolean> observeTyping(String self, String destination) {
        return firebaseObservableListeners.listenToValueEvents(userChat.child(destination).child(self).child(Constants.FIREBASE_CHAT_TYPING), asBoolean());
    }

    @Override
    public void setTyping(String self, String destination, Boolean value) {
        userChat.child(self).child(destination).child(Constants.FIREBASE_CHAT_TYPING).setValue(value);
    }

    @Override
    public void setUnreadCount(String self, String destination, Integer value) {
        userChat.child(destination).child(self).child(Constants.FIREBASE_CHAT_UNREADCOUNT).setValue(value);
    }

    @Override
    public void setMessageEnabled(String self, String destination, Boolean value) {
        userChat.child(self).child(destination).child(Constants.FIREBASE_CHAT_ENABLED).setValue(value);
    }

    private Func1<DataSnapshot,Boolean> asBoolean() {
        return new Func1<DataSnapshot, Boolean>() {
            @Override
            public Boolean call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Boolean.class);
            }
        };
    }

    private Func1<DataSnapshot,Integer> asInteger() {
        return new Func1<DataSnapshot, Integer>() {
            @Override
            public Integer call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Integer.class);
            }
        };
    }
}
