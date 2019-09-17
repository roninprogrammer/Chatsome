package com.dananaka.chatsome.conversation_list;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dananaka.chatsome.Dependencies;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.conversation_list.presenter.ConversationListPresenter;
import com.dananaka.chatsome.conversation_list.view.ConversationListDisplayer;
import com.dananaka.chatsome.navigation.AndroidConversationsNavigator;
import com.dananaka.chatsome.navigation.AndroidNavigator;

/**
 * Created by Vicknesh on 29/12/16.
 */

public class ConversationListFragment extends Fragment {

    private ConversationListPresenter presenter;
    private AndroidConversationsNavigator navigator;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations, container, false);
        getActivity().setTitle(R.string.conversations_toolbar_title);

        navigator = new AndroidConversationsNavigator((AppCompatActivity)getActivity(),new AndroidNavigator(getActivity()));
        presenter = new ConversationListPresenter(
                (ConversationListDisplayer) rootView.findViewById(R.id.conversationsView),
                Dependencies.INSTANCE.getConversationListService(),
                navigator,
                Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getUserService());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
