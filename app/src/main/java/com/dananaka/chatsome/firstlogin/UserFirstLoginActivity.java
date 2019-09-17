package com.dananaka.chatsome.firstlogin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import android.support.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.dananaka.chatsome.Constants;
import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vicknesh on 13/01/17.
 */

public class UserFirstLoginActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private Toolbar toolbar;
    private EditText nameEditText;
    private EditText statusEditText;
    private PlaceAutocompleteFragment places;
    private EditText placeText;
    private CircleImageView profileImageView;
    private Button startButton;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();

    private Place place = null;
    private String placeLat = null;
    private String placeLong = null;
    private String placeName = null;
    private boolean hasImageChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstlogin);

        // TODO to implement
        String image = getIntent().getStringExtra(Constants.FIREBASE_USERS_IMAGE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.firstlogin_title);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        statusEditText = (EditText) findViewById(R.id.statusEditText);
        profileImageView = (CircleImageView) findViewById(R.id.profileImageView);

        //Location field set at google autocomplete fragment.
        places = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        ((View)findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
        placeText = (EditText)places.getView().findViewById(R.id.place_autocomplete_search_input);
        placeText.setTextSize(18.0f);
        placeText.setHint(R.string.set_location);
        placeText.setBackgroundResource(R.color.colorText);

        if (firebaseUser.getDisplayName() != null && firebaseUser.getDisplayName().length() > 0)
            nameEditText.setText(firebaseUser.getDisplayName());
        if (statusEditText == null || statusEditText.getText().length() == 0)
            statusEditText.setText(R.string.profile_default_status_text);
        if (firebaseUser.getPhotoUrl() != null) {
            final Uri photoUrl = firebaseUser.getPhotoUrl();
            Glide.with(UserFirstLoginActivity.this).load(photoUrl).into(profileImageView);
            hasImageChanged = true;
        }
        startButton = (Button) findViewById(R.id.startButton);

        if(firebaseUser.isEmailVerified()){
            if (ContextCompat.checkSelfPermission(UserFirstLoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                profileImageView.setEnabled(false);
                ActivityCompat.requestPermissions(UserFirstLoginActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            } else {
                profileImageView.setEnabled(true);
            }
        }else{

            profileImageView.setEnabled(false);

            new MaterialDialog.Builder(this)
                    .title(R.string.verify_email_dialog_name)
                    .content(R.string.verify_email_dialog_content)
                    .positiveText(R.string.verify_email_dialog_agree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            disableToken();
                            firebaseAuth.signOut();
                            Dependencies.INSTANCE.clearDependecies();
                            dialog.dismiss();
                            Intent intent = new Intent(UserFirstLoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }

        //get Location form google autocomplete fragment.
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place location) {
                    place = location;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();
                place = null;
            }
        });

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        places.setFilter(typeFilter);

        startButton.setOnClickListener(this);
        profileImageView.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                profileImageView.setEnabled(true);
            }
        }
    }

    private void setProgressBar(){
        final MaterialDialog progressBar = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressBarStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressBarStatus < 100){
                    progressBarStatus += 30;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.dismiss();
                }

            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO){
            if(resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    //set Progress Bar
                    setProgressBar();
                    //set profile picture form gallery
                    profileImageView.setImageBitmap(selectedImage);
                    hasImageChanged = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }else if(requestCode == CAPTURE_PHOTO){
            if(resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        }
    }



    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        //set Progress Bar
        setProgressBar();
        //set profile picture form camera
        profileImageView.setImageBitmap(thumbnail);
        hasImageChanged = true;
    }


    @Override
    public void onClick(final View view) {
        switch(view.getId()) {
            case R.id.startButton:
                    if (firebaseUser.getProviders().get(0).equals("facebook.com") || firebaseUser.isEmailVerified()) {
                        if (nameEditText.getText() == null) {
                            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (nameEditText.getText().toString().trim().length() == 0) {
                            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (statusEditText.getText() == null) {
                            Toast.makeText(this, "Status cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (statusEditText.getText().toString().trim().length() == 0) {
                            Toast.makeText(this, "Status cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (place == null && placeText.getText().length() == 0) {
                            Toast.makeText(this, "Location cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            placeName = (String) place.getName();
                            placeLat = String.valueOf(place.getLatLng().latitude);
                            placeLong = String.valueOf(place.getLatLng().longitude);
                        }

                        if (firebaseUser != null) {
                            final HashMap<String, String> values = new HashMap<>();
                            values.put(Constants.FIREBASE_USERS_PLACELONG, placeLong);
                            values.put(Constants.FIREBASE_USERS_PLACELAT, placeLat);
                            values.put(Constants.FIREBASE_USERS_PLACENAME, placeName);
                            values.put(Constants.FIREBASE_USERS_STATUS, statusEditText.getText().toString());
                            values.put(Constants.FIREBASE_USERS_EMAIL, firebaseUser.getEmail());
                            values.put(Constants.FIREBASE_USERS_NAME, nameEditText.getText().toString());
                            values.put(Constants.FIREBASE_USERS_UID, firebaseUser.getUid());

                            if (hasImageChanged) {
                                if (firebaseUser.getPhotoUrl() != null) {
                                    final String userPhotoUrl = firebaseUser.getPhotoUrl().toString();
                                    values.put(Constants.FIREBASE_USERS_IMAGE, userPhotoUrl);
                                    databaseReference.child(Constants.FIREBASE_USERS)
                                            .child(firebaseUser.getUid()).setValue(values);
                                    setToken();

                                    finish();
                                } else {
                                    profileImageView.setDrawingCacheEnabled(true);
                                    profileImageView.buildDrawingCache();
                                    Bitmap bitmap = profileImageView.getDrawingCache();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    final String imageRef = profileImageView.getDrawingCache().hashCode() + System.currentTimeMillis() + ".jpg";
                                    StorageReference mountainsRef = storageReference.child(imageRef);
                                    UploadTask uploadTask = mountainsRef.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            values.put(Constants.FIREBASE_USERS_IMAGE, imageRef);
                                            databaseReference.child(Constants.FIREBASE_USERS)
                                                    .child(firebaseUser.getUid()).setValue(values);
                                            setToken();

                                            finish();
                                        }
                                    });
                                }
                            } else {
                                values.put(Constants.FIREBASE_USERS_IMAGE, "");
                                databaseReference.child(Constants.FIREBASE_USERS)
                                        .child(firebaseUser.getUid()).setValue(values);
                                setToken();

                                finish();
                            }
                        }
                    }



                break;
            case R.id.profileImageView:
                new MaterialDialog.Builder(this)
                        .title(R.string.uploadImages)
                        .items(R.array.uploadImages)
                        .itemsIds(R.array.itemIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which){
                                    case 0:
                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                        photoPickerIntent.setType("image/*");
                                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                                        break;
                                    case 1:
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAPTURE_PHOTO);
                                        break;
                                    case 2:
                                        profileImageView.setImageResource(R.drawable.ic_account_circle_black);
                                        hasImageChanged = true;
                                        break;
                                }
                            }
                        })
                        .show();
                break;
        }
    }

    private void disableToken() {
        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FCM);
        tokenRef.child(firebaseUser.getUid() + "/" + Constants.FIREBASE_FCM_ENABLED).setValue(Boolean.FALSE.toString());
    }

    private void setToken() {
        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_FCM);
        tokenRef.child(firebaseUser.getUid() + "/" + Constants.FIREBASE_FCM_TOKEN).setValue(FirebaseInstanceId.getInstance().getToken());
        tokenRef.child(firebaseUser.getUid() + "/" + Constants.FIREBASE_FCM_ENABLED).setValue(Boolean.TRUE.toString());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("UserFirstLogin", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }
}
