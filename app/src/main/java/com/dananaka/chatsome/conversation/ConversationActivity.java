package com.dananaka.chatsome.conversation;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.dananaka.chatsome.BaseActivity;
import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.conversation.presenter.ConversationPresenter;
import com.dananaka.chatsome.conversation.view.ConversationDisplayer;
import com.dananaka.chatsome.conversation.view.ConversationView;
import com.dananaka.chatsome.navigation.AndroidNavigator;

/**
 * Created by Vicknesh on 29/12/16.
 */

public class ConversationActivity extends BaseActivity {

    private ConversationPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


        ConversationDisplayer conversationDisplayer = (ConversationView) findViewById(R.id.conversationView);
        presenter = new ConversationPresenter(
                Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getConversationService(),
                conversationDisplayer,
                Dependencies.INSTANCE.getUserService(),
                getIntent().getStringExtra("sender"),
                getIntent().getStringExtra("destination"),
                new AndroidNavigator(this)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
