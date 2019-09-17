package com.dananaka.chatsome.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.user.data_model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vicknesh on 14/01/17.
 */

public class AndroidProfileNavigator implements ProfileNavigator {

    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();

    private final AppCompatActivity activity;

    private ProfileDialogListener dialogListener;

    public AndroidProfileNavigator(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void showNameTextDialog(String hint, final TextView textView, final User user) {
        final MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.profile_dialog_name_title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(activity.getString(R.string.profile_hint_name), textView.getText().toString(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .negativeText(R.string.profile_dialog_input_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String inputText = dialog.getInputEditText().getText().toString();
                        if (inputText.length() == 0) {
                            Toast.makeText(activity,"Name cannot be empty",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialogListener.onNameSelected(inputText,user);
                        textView.setText(inputText);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showStatusextDialog(String hint,final TextView textView, final User user) {
        final MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.profile_dialog_status_title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(activity.getString(R.string.profile_hint_status), textView.getText().toString(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .negativeText(R.string.profile_dialog_input_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String inputText = dialog.getInputEditText().getText().toString();
                        if (inputText.length() == 0) {
                            Toast.makeText(activity,"Status cannot be empty",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialogListener.onStatusSelected(inputText,user);
                        textView.setText(inputText);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showInputPasswordDialog(String hint, User user) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.profile_dialog_password_title)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .negativeText(R.string.profile_dialog_input_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String password = dialog.getInputEditText().getText().toString();
                        if (password.length() < 8) {
                            Toast.makeText(activity,R.string.login_snackbar_password_short,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialogListener.onPasswordSelected(password);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showImagePicker() {
        new MaterialDialog.Builder(activity)
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
                                activity.startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                                break;
                            case 1:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                activity.startActivityForResult(intent, CAPTURE_PHOTO);
                                break;
                            case 2:
                                dialogListener.onImageSelected(null);
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    public void showSaveDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .content(R.string.profile_dialog_save_content)
                .positiveText(R.string.profile_dialog_profile_positive)
                .negativeText(R.string.profile_dialog_profile_negative)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(activity,R.string.profile_toast_save_positive,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        activity.finish(); //TODO
                    }
                })
                .show();
    }

    @Override
    public void showRemoveDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .content(R.string.profile_dialog_remove_content)
                .positiveText(R.string.profile_dialog_profile_positive)
                .negativeText(R.string.profile_dialog_profile_negative)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialogListener.onRemoveSelected();
                        Toast.makeText(activity,R.string.profile_toast_password_positive,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        activity.finish(); //TODO
                    }
                })
                .show();
    }

    @Override
    public void attach(ProfileDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void detach(ProfileDialogListener dialogListener) {
        this.dialogListener = null;
    }


    @Override
    public void toLogin() {

    }

    @Override
    public void toMainActivity() {

    }

    private void setProgressBar(){
        final MaterialDialog progressBar = new MaterialDialog.Builder(activity)
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
    public void toParent() {
        activity.finish();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == SELECT_PHOTO){
            if(resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = intent.getData();
                    final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    //set Progress Bar
                    setProgressBar();
                    //set profile picture form gallery
                    dialogListener.onImageSelected(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }else if(requestCode == CAPTURE_PHOTO){
            if(resultCode == RESULT_OK) {
                Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
                //set Progress Bar
                setProgressBar();
                //set profile picture form camera
                dialogListener.onImageSelected(thumbnail);

            }
        }

        return true;
    }

}
