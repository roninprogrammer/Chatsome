package com.dananaka.chatsome.navigation;

/**
 * Created by Vicknesh on 16/12/16.
 */

public interface MainNavigator {

    void attach();

    void detach();

    void init();

    void toConversations();

    void toUserList();

    void toAbout();

    void toInvite();

    void toProfile();

    void toFirstLogin();

    void toIntroActivity();

    void toGoogleSignOut(int method);

    void toLogin();

    void showProgessDialog();

    void hideProgessDialog();

    Boolean onBackPressed();

}
