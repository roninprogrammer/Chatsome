package com.dananaka.chatsome.storage;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

import rx.Observable;

/**
 * Created by Vicknesh on 18/12/16.
 */

public interface StorageService {

    StorageReference getProfileImageReference(String image);

    Observable<String> uploadImage(Bitmap bitmap);

}
