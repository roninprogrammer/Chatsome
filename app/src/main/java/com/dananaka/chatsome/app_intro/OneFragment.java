package com.dananaka.chatsome.app_intro;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dananaka.chatsome.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {


    private TextView app_intro_version;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

//        app_intro_version = (TextView) view.findViewById(R.id.app_intro_version);
//        //set version in fragment one
//        app_intro_version.setText(BuildConfig.VERSION_NAME);

        return view;


    }
}
