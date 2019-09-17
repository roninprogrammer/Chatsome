package com.dananaka.chatsome;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
//import com.dananaka.chatsome.analytics.Analytics;
//import com.dananaka.chatsome.analytics.FirebaseAnalyticsAnalytics;
import com.dananaka.chatsome.conversation.database.FirebaseConversationDatabase;
import com.dananaka.chatsome.conversation.service.ConversationService;
import com.dananaka.chatsome.conversation.service.PersistedConversationService;
import com.dananaka.chatsome.conversation_list.database.FirebaseConversationListDatabase;
import com.dananaka.chatsome.conversation_list.service.ConversationListService;
import com.dananaka.chatsome.conversation_list.service.PersistedConversationListService;

import com.dananaka.chatsome.login.database.FirebaseAuthDatabase;
import com.dananaka.chatsome.login.service.FirebaseLoginService;
import com.dananaka.chatsome.login.service.LoginService;
import com.dananaka.chatsome.main.database.FirebaseCloudMessagingDatabase;
import com.dananaka.chatsome.main.service.CloudMessagingService;
import com.dananaka.chatsome.main.service.FirebaseCloudMessagingService;
import com.dananaka.chatsome.main.service.MainService;
import com.dananaka.chatsome.main.service.PersistedMainService;
import com.dananaka.chatsome.profile.service.FirebaseProfileService;
import com.dananaka.chatsome.profile.service.ProfileService;
import com.dananaka.chatsome.registration.service.FirebaseRegistrationService;
import com.dananaka.chatsome.registration.service.RegistrationService;
import com.dananaka.chatsome.rxrelay.FirebaseObservableListeners;
import com.dananaka.chatsome.storage.FirebaseStorageService;
import com.dananaka.chatsome.storage.StorageService;
import com.dananaka.chatsome.user.database.FirebaseUserDatabase;
import com.dananaka.chatsome.user.service.PersistedUserService;
import com.dananaka.chatsome.user.service.UserService;

/**
 * Created by Vicknesh on 27/12/16.
 */

public enum Dependencies {
    INSTANCE;

//    private Analytics analytics;
    //private ErrorLogger errorLogger;

    private RegistrationService registrationService;
    private LoginService loginService;
    private ConversationListService conversationListService;
    private ConversationService conversationService;

    private UserService userService;
    private MainService mainService;
    private ProfileService profileService;
    private CloudMessagingService messagingService;
    private StorageService storageService;
    private String firebaseToken;
    private boolean setPersistence = false;

    public void init(Context context) {
        if (needsInitialisation()) {
            Context appContext = context.getApplicationContext();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseToken = FirebaseInstanceId.getInstance().getToken();
            if (!setPersistence) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                setPersistence = true;
            }
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            FirebaseObservableListeners firebaseObservableListeners = new FirebaseObservableListeners();
            FirebaseUserDatabase userDatabase = new FirebaseUserDatabase(firebaseDatabase, firebaseObservableListeners);
            FirebaseCloudMessagingDatabase messagingDatabase = new FirebaseCloudMessagingDatabase(firebaseDatabase, firebaseObservableListeners, firebaseToken);
            FirebaseConversationDatabase conversationDatabase = new FirebaseConversationDatabase(firebaseDatabase, firebaseObservableListeners);
            FirebaseConversationListDatabase conversationListDatabase = new FirebaseConversationListDatabase(firebaseDatabase,firebaseObservableListeners);

            loginService = new FirebaseLoginService(new FirebaseAuthDatabase(firebaseAuth),messagingDatabase);
            registrationService = new FirebaseRegistrationService(firebaseAuth);
            conversationService = new PersistedConversationService(conversationDatabase);

            userService = new PersistedUserService(userDatabase);
            conversationListService = new PersistedConversationListService(conversationListDatabase,conversationDatabase,userDatabase);
            mainService = new PersistedMainService(firebaseAuth, userDatabase, messagingDatabase);
            profileService = new FirebaseProfileService(firebaseAuth);

            messagingService = new FirebaseCloudMessagingService(messagingDatabase);
            storageService = new FirebaseStorageService(firebaseStorage,firebaseObservableListeners);
        }
    }

    public void clearDependecies() {
        loginService = null;
        conversationListService = null;
        conversationService = null;
        userService = null;
    }

    private boolean needsInitialisation() {
        return loginService == null || conversationListService == null || conversationService == null || registrationService == null
                || userService == null;// || analytics == null || errorLogger == null;
    }


    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void initFirebaseToken() {
        firebaseToken = FirebaseInstanceId.getInstance().getToken();
    }

    public MainService getMainService() {
        return mainService;
    }

    public CloudMessagingService getMessagingService() {
        return messagingService;
    }



    public ConversationService getConversationService() {
        return conversationService;
    }

    public ConversationListService getConversationListService() {
        return conversationListService;
    }

    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public UserService getUserService() {
        return userService;
    }

    public StorageService getStorageService() {
        return storageService;
    }

}
