package com.dananaka.chatsome.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.Utils;
import com.dananaka.chatsome.user.data_model.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vicknesh on 19/12/16.
 */

public class UserView extends FrameLayout {

    private TextView nameTextView;
    private TextView statusTextView;
    private CircleImageView profileImageView;
    private CircleImageView indicator;


    private int layoutResId;

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_users_item_view);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        indicator = (CircleImageView) this.findViewById(R.id.indicator);
        nameTextView = (TextView) this.findViewById(R.id.nameTextView);
        statusTextView = (TextView) this.findViewById(R.id.statusTextView);
        profileImageView = (CircleImageView) this.findViewById(R.id.profileImageView);
    }

    public void display(User user) {
        if(user.getLastSeen() == 0){
            indicator.setImageResource(R.drawable.ic_greencircle);
        }else{
            indicator.setImageResource(R.drawable.ic_graycircle);
        }
        Utils.loadImageElseBlack(user.getImage(), profileImageView, getContext());
        nameTextView.setText(user.getName());
        nameTextView.setSelected(true);
        statusTextView.setText(user.getStatus());
        statusTextView.setSelected(true);
    }

}

