package com.dananaka.chatsome.storage;

import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.dananaka.chatsome.rxrelay.FirebaseObservableListeners;

import java.io.ByteArrayOutputStream;

import rx.Observable;

/**
 * Created by Vicknesh on 18/12/16.
 */

public class FirebaseStorageService implements StorageService {

    private final StorageReference firebaseStorage;

    private final FirebaseObservableListeners firebaseObservableListeners;


    public FirebaseStorageService(FirebaseStorage firebaseStorage, FirebaseObservableListeners firebaseObservableListeners) {
        this.firebaseStorage = firebaseStorage.getReference();
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public StorageReference getProfileImageReference(String image) {
        return firebaseStorage.child(image);
    }

    @Override
    public Observable<String> uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final String imageRef = bitmap.hashCode() + System.currentTimeMillis() + ".jpg";
        return firebaseObservableListeners.uploadTask(data,firebaseStorage.child(imageRef),imageRef);
    }

}
