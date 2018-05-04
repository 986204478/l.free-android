package com.lfork.a98620.lfree.chatwindow;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.imservice.message.Message;
import com.lfork.a98620.lfree.imservice.message.MessageContentType;
import com.lfork.a98620.lfree.imservice.message.MessageType;
import com.lfork.a98620.lfree.data.imdata.IMDataRepository;
import com.lfork.a98620.lfree.data.imdata.MessageDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.imservice.MessageListener;

import java.util.List;

/**
 * Created by 98620 on 2018/4/22.
 */
public class ChatWindowViewModel extends BaseViewModel implements MessageListener {

    public final ObservableArrayList<Message> messages = new ObservableArrayList<>();

    public final ObservableField<String> newMessage = new ObservableField<>();

    public final ObservableField<String> username = new ObservableField<>();

    private static final String TAG = "ChatWindowViewModel";

    private IMDataRepository repository;

    private MessageDataRepository msgMepository;

    private ChatWindowNavigator navigator;

    private int userId;

    private int thisUserId;

    private boolean isAdded = false;


    ChatWindowViewModel(Context context) {
        super(context);
        repository = IMDataRepository.getInstance();
        msgMepository = MessageDataRepository.getInstance(0);
    }

    public void start() {
        isAdded = false;
        loadMessages();
        //判断当前用户有没有在好友列表中，然后进行addUser操作
        new Thread(() -> {
            if (!repository.isUserExisted(userId)) {
                addUser(false);
            }
        }).start();

    }

    public void setNavigator(ChatWindowNavigator navigator) {
        this.navigator = navigator;
    }

    public void setUserInfo(String username, int userId, int thisUserId) {
        this.username.set(username);
        this.userId = userId;
        this.thisUserId = thisUserId;
    }


    private void loadMessages() {
        new Thread(() -> {
            MessageContentType type = MessageContentType.COMMUNICATION_USER;
            msgMepository.getMessages(userId, type, new DataSource.GeneralCallback<List<Message>>() {
                @Override
                public void succeed(List<Message> data) {
                    messages.clear();
                    messages.addAll(data);

                navigator.notifyMessagesChanged();
//                notifyPropertyChanged(BR.viewModel); // It's a @Bindable so update manually
//                    navigator.showToast("聊天记录加载成功");
                }

                @Override
                public void failed(String log) {
                    navigator.showToast("log");
                }
            });
        }).start();

    }

    public void sendMessage() {

        String message_sent = newMessage.get();
        if (!TextUtils.isEmpty(message_sent)) {

            Message m;
            m = new Message(message_sent, Message.SendType);
            m.setSenderID(thisUserId);
            m.setReceiverID(userId);
            m.setType(MessageType.NORMAL_MESSAGE);
            m.setContentType(MessageContentType.COMMUNICATION_USER);
            m.setMessageID(System.currentTimeMillis());
            messages.add(m);

            navigator.sendMessage(m, new DataSource.GeneralCallback<Message>() {
                @Override
                public void succeed(Message data) {
                    navigator.showToast("发送成功");
                }

                @Override
                public void failed(String log) {
                    navigator.showToast(log);

                }
            });

            notifyChange();
            newMessage.set(""); //清空输入框的内容

            addUser(true);

        }
    }

    private void addUser(boolean isExisted) {

        if (isAdded){
            return;
        }


        new Thread(() -> {
            UserDataRepository userDataRepository = UserDataRepository.getInstance();
            userDataRepository.getUserInfo(new DataSource.GeneralCallback<User>() {
                @Override
                public void succeed(User data) {
                    repository.addChatUser(data, isExisted, new DataSource.GeneralCallback<String>() {
                        @Override
                        public void succeed(String data) {
                            Log.d(TAG, "succeed: 用户添加成功");
                            isAdded = true;
                        }

                        @Override
                        public void failed(String log) {

                        }
                    });
                }

                @Override
                public void failed(String log) {

                }
            }, userId);
        }).start();


    }

    private void refreshMessages(Message message) {

    }


    @Override
    public void onReceived(Message message) {
        message.setChatType(Message.ReceiveType);
        messages.add(message);
        navigator.notifyMessagesChanged();
    }
}
