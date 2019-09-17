package com.dananaka.chatsome.profile.presenter;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.dananaka.chatsome.Utils;
import com.dananaka.chatsome.login.data_model.Authentication;
import com.dananaka.chatsome.login.service.LoginService;
import com.dananaka.chatsome.navigation.ProfileNavigator;
import com.dananaka.chatsome.profile.service.ProfileService;
import com.dananaka.chatsome.profile.view.ProfileDisplayer;
import com.dananaka.chatsome.storage.StorageService;
import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.service.UserService;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Vicknesh on 14/01/17.
 */

public class ProfilePresenter {

    private final LoginService loginService;
    private final UserService userService;
    private final ProfileService profileService;
    private final StorageService storageService;
    private final ProfileDisplayer profileDisplayer;
    private final ProfileNavigator navigator;

    private User self;
    private Subscription loginSubscription;
    private Subscription userSubscription;
    private String userImage;
    private StorageReference desertRef;

    public ProfilePresenter(LoginService loginService,
                            UserService userService,
                            ProfileService profileService,
                            StorageService storageService,
                            ProfileDisplayer loginDisplayer,
                            ProfileNavigator navigator) {
        this.loginService = loginService;
        this.userService = userService;
        this.profileService = profileService;
        this.storageService = storageService;
        this.profileDisplayer = loginDisplayer;
        this.navigator = navigator;
    }



    public void startPresenting() {
        navigator.attach(dialogListener);
        profileDisplayer.attach(actionListener);
        loginSubscription = loginService.getAuthentication()
                .subscribe(new Action1<Authentication>() {
                    @Override
                    public void call(final Authentication authentication) {
                        if (authentication.isSuccess()) {
                            userService.getUser(authentication.getUser().getUid())
                                    .subscribe(new Subscriber<User>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(User user) {
                                            self = user;
                                            profileDisplayer.display(user);
                                        }
                                    });
                        } else {
                            navigator.toParent();
                        }
                    }
                });
    }

    public void stopPresenting() {
        navigator.detach(dialogListener);
        profileDisplayer.detach(actionListener);
        loginSubscription.unsubscribe();
        if (userSubscription != null)
            userSubscription.unsubscribe();
    }

    private ProfileDisplayer.ProfileActionListener actionListener = new ProfileDisplayer.ProfileActionListener() {

        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onNamePressed(String hint, TextView textView) {
            navigator.showNameTextDialog(hint,textView,self);
        }

        @Override
        public void onStatusPressed(String hint, TextView textView) {
            navigator.showStatusextDialog(hint,textView,self);
        }

        @Override
        public void onPasswordPressed(String hint) {
            navigator.showInputPasswordDialog(hint,self);
        }

        @Override
        public void onImagePressed() {
            navigator.showImagePicker();
        }

        @Override
        public void onSavePressed() {
            navigator.showSaveDialog();
        }

        @Override
        public void onRemovePressed() {
            navigator.showRemoveDialog();
        }
    };

    private ProfileNavigator.ProfileDialogListener dialogListener = new ProfileNavigator.ProfileDialogListener() {

        @Override
        public void onNameSelected(String text, User user) {
            userService.setName(user,text);
        }

        @Override
        public void onStatusSelected(String text, User user) {
            userService.setStatus(user,text);
        }

        @Override
        public void onPasswordSelected(String text) {
            profileService.setPassword(text);
        }


        @Override
        public void onRemoveSelected() {
            profileService.removeUser();
        }

        @Override
        public void onImageSelected(final Bitmap bitmap) {
            userImage = self.getImage();
            if(bitmap != null) {
                if (userImage.length() != 0 && !userImage.equals("") && userImage != null) {
                    if(!userImage.startsWith("https://")) {
                        desertRef = storageService.getProfileImageReference(userImage);
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                storageService.uploadImage(bitmap)
                                        .subscribe(new Action1<String>() {
                                            @Override
                                            public void call(final String image) {
                                                if (image != null) {
                                                    profileDisplayer.updateProfileImage(bitmap);
                                                    userService.setProfileImage(self, image);
                                                }
                                            }
                                        });
                            }
                        });
                    }else{
                        storageService.uploadImage(bitmap)
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(final String image) {
                                    if (image != null) {
                                        profileDisplayer.updateProfileImage(bitmap);
                                        userService.setProfileImage(self, image);
                                    }
                                }
                            });
                    }
                } else {
                storageService.uploadImage(bitmap)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(final String image) {
                            if (image != null) {
                                profileDisplayer.updateProfileImage(bitmap);
                                userService.setProfileImage(self, image);
                            }
                        }
                    });
                }
            } else {
                if (userImage.length() != 0 && !userImage.equals("") && userImage != null) {
                    if(!userImage.startsWith("https://")) {
                        desertRef = storageService.getProfileImageReference(userImage);
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                String img = "";
                                userService.setProfileImage(self, img);
                            }
                        });
                    } else {
                        String img = "";
                        userService.setProfileImage(self, img);
                    }
                }

            }
        }

    };

}
