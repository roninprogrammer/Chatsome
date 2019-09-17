package com.dananaka.chatsome;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.StorageReference;
import com.dananaka.chatsome.storage.FirebaseImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vicknesh on 26/12/16.
 */

public class Utils {

    public static String getCurrentTimestamp() {
        //SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        SimpleDateFormat dateFormatGmt =
                new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss", Locale.US);

        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatGmt.format(new Date());
    }

    @Nullable
    public static String getTimestamp(String timestamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm", Locale.US),
                             sdf2 = new SimpleDateFormat("HH:mm", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = sdf.parse(timestamp);

            return  String.valueOf(sdf2.format(date));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Nullable
    public static String getDate(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm", Locale.US);
            Date date = sdf.parse(timestamp);
            long currentDate = date.getTime();
            currentDate += TimeZone.getDefault().getOffset(currentDate);

            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return sdfDate.format(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadImageElseBlack(String image, CircleImageView imageView, Context context) {

        try {
            if (image != null && image.length() > 0) {
                if (image.startsWith("https://")) {
                    final Uri userPhotoUri = Uri.parse(image);
                    Glide.with(context)
                            .load(userPhotoUri)
                            .into(imageView);
                } else {
                    StorageReference ref = Dependencies.INSTANCE.getStorageService().getProfileImageReference(image);
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(ref)
                            .into(imageView);
                }

            } else {
                Glide.with(context)
                        .load("")
                        .placeholder(R.drawable.ic_account_circle_white)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void loadImageElseWhite(String image, CircleImageView imageView, Context context) {

        try {
            if (image != null && image.length() > 0) {
                if (image.startsWith("https://")) {
                    final Uri userPhotoUri = Uri.parse(image);
                    Glide.with(context)
                            .load(userPhotoUri)
                            .into(imageView);
                } else {
                    StorageReference ref = Dependencies.INSTANCE.getStorageService().getProfileImageReference(image);
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(ref)
                            .into(imageView);
                }
            } else {
                Glide.with(context)
                        .load("")
                        .placeholder(R.drawable.ic_account_circle_black)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

}
