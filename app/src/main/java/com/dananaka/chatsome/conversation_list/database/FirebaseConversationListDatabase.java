package com.dananaka.chatsome.conversation_list.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.dananaka.chatsome.Constants;
import com.dananaka.chatsome.rxrelay.FirebaseObservableListeners;
import com.dananaka.chatsome.user.data_model.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Vicknesh on 29/12/16.
 */

public class FirebaseConversationListDatabase implements ConversationListDatabase {

    private DatabaseReference firebaseDatabase;
    private FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseConversationListDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        this.firebaseDatabase = firebaseDatabase.getReference();
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    private DatabaseReference unreadCountMsg(String self, String destination) {
        return firebaseDatabase.child(Constants.FIREBASE_CHAT).child(self).child(destination).child(Constants.FIREBASE_CHAT_UNREADCOUNT);
    }

    @Override
    public Observable<List<String>> observeConversationsFor(User user) {
        return firebaseObservableListeners.listenToValueEvents(firebaseDatabase
            .child(Constants.FIREBASE_CHAT).child(user.getUid()), getConversations());
    }

    @Override
    public Observable<Integer> observeUnreadCount(String self, String destination) {
        return firebaseObservableListeners.listenToValueEvents(unreadCountMsg(self,destination), asInteger());
    }

    @Override
    public void setUnreadCount(String self, String destination, Integer value) {
        firebaseDatabase.child(Constants.FIREBASE_CHAT).child(self).child(destination).child(Constants.FIREBASE_CHAT_UNREADCOUNT).setValue(value);
    }

    private Func1<DataSnapshot, List<String>> getConversations() {
        return new Func1<DataSnapshot, List<String>>() {
            @Override
            public List<String> call(DataSnapshot dataSnapshot) {
                final List<String> conversationsUserUid = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (final DataSnapshot child : children) {
                        conversationsUserUid.add(child.getKey());
                    }
                }
                return conversationsUserUid;
            }
        };
    }

    private static Func1<DataSnapshot, User> asUser() {
        return new Func1<DataSnapshot, User>() {
            @Override
            public User call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(User.class);
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
