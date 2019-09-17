package com.dananaka.chatsome.user.view;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.dananaka.chatsome.user.data_model.User;

/**
 * Created by Vicknesh on 19/12/16.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    private final UserView userView;
    public UserViewHolder(UserView itemView) {
        super(itemView);
        this.userView = itemView;
    }

    public void bind(final User user, final UserSelectionListener listener) {
//        Log.i("userId",""+user.getUid());
        userView.display(user);
        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserSelected(user);
            }
        });
    }

    public interface UserSelectionListener {
        void onUserSelected(User user);
    }

}