package com.dananaka.chatsome.navigation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dananaka.chatsome.main.MainActivity;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.dananaka.chatsome.app_intro.IntroActivity;
import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.about.AboutActivity;
import com.dananaka.chatsome.conversation_list.ConversationListFragment;
import com.dananaka.chatsome.login.LoginActivity;
import com.dananaka.chatsome.firstlogin.UserFirstLoginActivity;
import com.dananaka.chatsome.profile.ProfileActivity;
import com.dananaka.chatsome.user.UsersFragment;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vicknesh on 16/12/16.
 */

public class AndroidMainNavigator implements MainNavigator {

    private static final String TAG = AndroidMainNavigator.class.getSimpleName();
    private static final int REQUEST_INVITE = 1;
    public static final int LOGOUT_GOOGLE = 1;

    private boolean doubleBackToExitPressedOnce = false;
    private boolean firstOpen = true;

    private final AppCompatActivity activity;
    private final GoogleApiClient googleApiClient;

    private MaterialDialog progressDialog;

    public AndroidMainNavigator(MainActivity activity, @Nullable GoogleApiClient googleApiClient) {
        this.activity = activity;
        this.googleApiClient = googleApiClient;
    }


    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    @Override
    public void init() {
        if (firstOpen) {
            this.toConversations();
            firstOpen = false;
        }
    }

    @Override
    public void toConversations() {
        ConversationListFragment conversationsFragment = new ConversationListFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,conversationsFragment)
                .commit();
    }


    @Override
    public void toUserList() {
        UsersFragment usersFragment = new UsersFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,usersFragment)
                .commit();
    }

    @Override
    public void toAbout(){
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }

    @Override
    public void toInvite() {
        Intent intent = new AppInviteInvitation.IntentBuilder(activity.getString(R.string.main_invite_title))
                .setMessage(activity.getString(R.string.main_invite_message))
//                .setDeepLink(Uri.parse(activity.getString(R.string.invitation_deep_link)))
//                .setCustomImage(Uri.parse(activity.getString(R.string.invitation_custom_image)))
                .setCallToActionText(activity.getString(R.string.main_invite_cta))
                .build();
        activity.startActivityForResult(intent, REQUEST_INVITE);

    }

    @Override
    public void toProfile() {
        activity.startActivity(new Intent(activity,ProfileActivity.class));
    }

    @Override
    public void toFirstLogin() {
        activity.startActivity(new Intent(activity,UserFirstLoginActivity.class));
    }

    @Override
    public void toIntroActivity() {
        activity.startActivity(new Intent(activity,IntroActivity.class));
    }

    @Override
    public void toGoogleSignOut(int method) {
        Toast.makeText(activity,R.string.main_toast_logout_message,Toast.LENGTH_LONG).show();
        if (method == LOGOUT_GOOGLE) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
    }

    @Override
    public void toLogin() {
        Dependencies.INSTANCE.clearDependecies();
        activity.startActivity(new Intent(activity,LoginActivity.class));

    }

    @Override
    public void showProgessDialog() {
        progressDialog = new MaterialDialog.Builder(activity)
                .content(R.string.main_dialog_text_wait)
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgessDialog() {
        progressDialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Boolean onBackPressed() {
        if (doubleBackToExitPressedOnce)
            activity.finishAffinity();

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(activity, R.string.main_toast_exit_message, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        return true;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Use Firebase Measurement to log that invitation was sent.
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_sent");

                // Check how many invitations were sent and log.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
//                Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                // Use Firebase Measurement to log that invitation was not sent
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "inv_not_sent");

                // Sending failed or it was canceled, show failure message to the user
//                Log.d(TAG, "Failed to send invitation.");
            }
            return true;
        } else
            return false;
    }
}
