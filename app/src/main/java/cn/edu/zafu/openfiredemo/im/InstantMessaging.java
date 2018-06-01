package cn.edu.zafu.openfiredemo.im;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import java.util.LinkedList;
import java.util.List;

import cn.edu.zafu.openfiredemo.App;
import cn.edu.zafu.openfiredemo.greendao.ChatRecordDao;
import cn.edu.zafu.openfiredemo.im.bean.Contacts;
import cn.edu.zafu.openfiredemo.im.bean.IMMessage;
import cn.edu.zafu.openfiredemo.im.db.DBService;
import cn.edu.zafu.openfiredemo.im.db.bean.ChatRecord;

/**
 * 即时通讯类
 * Created by xrc on 18/5/22.
 */

public class InstantMessaging {

    private static InstantMessaging sInstantMessaging;

    private LocalConnectionReceiver localConnectionReceiver = new LocalConnectionReceiver();

    private IIMService mImService;

    private InstantMessagingConfig mConnectionConfig;

    private ConnectionListener mConnectionListener;

    private ChatListener mChatListener;

    //待处理的消息队列
    private LinkedList<String> messageQueue = new LinkedList<>();

    public static InstantMessaging init() {
        initInstance();
        return sInstantMessaging;
    }

    public InstantMessaging config(InstantMessagingConfig config) {
        if (sInstantMessaging == null) {
            throw new RuntimeException("请先初始化InstantMessaging");
        }
        mConnectionConfig = config;
        return sInstantMessaging;
    }

    public void start() {
        bindService();
        registerReceiver();
        createConnection();
        initConnectionByConfig();
    }

    private void initConnectionByConfig() {
        if (mImService == null) {
            messageQueue.add(Constant.QUEUE_INIT_CONFIG);
        } else {
            if (mConnectionConfig.isAutoReceipt()) {
                try {
                    mImService.autoReceipt();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            try {
                mImService.autoFriend(mConnectionConfig.isAutoFriend());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_IM);
        App.getInstance().registerReceiver(localConnectionReceiver, filter);
    }

    private void bindService() {
        Intent intent = new Intent(App.getInstance(), IMService.class);
        intent.putExtra(Constant.CONNECTION_CONFIG, mConnectionConfig);
        App.getInstance().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public static InstantMessaging with() {
        if (sInstantMessaging == null) {
            throw new RuntimeException("you must call the method initInstance() first!");
        }
        return sInstantMessaging;
    }

    private static void initInstance() {
        if (sInstantMessaging == null) {
            sInstantMessaging = new InstantMessaging();
        }
    }

    public InstantMessagingConfig getConnectionConfig() {
        return mConnectionConfig;
    }

    public ConnectionListener getConnectionListener() {
        return mConnectionListener;
    }

    public ChatListener getChatListener() {
        return mChatListener;
    }

    public void startRecord() {
        try {
            mImService.startRecord();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        try {
            mImService.stopRecord();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelRecord() {
        try {
            mImService.cancelRecord();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void listenRecord(String filePath) {
        try {
            mImService.listenRecord(filePath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String targetUser, String content) {
        try {
            String targetAddress = targetUser + "@" + mConnectionConfig.getServer();
            mImService.sendMessage(targetAddress, content);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void login(String account, String password) {
        try {
            mImService.login(account, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void reLogin(String account, String password) {
        createConnection();
        login(account, password);
    }

    /**
     * 创建连接
     */
    public void createConnection() {
        try {
            if (mImService == null) {
                messageQueue.add(Constant.QUEUE_CREATE_CONNECTION);
            } else {
                mImService.createConnection();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            mImService.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册
     *
     * @param account  用户名
     * @param password 密码
     */
    public void register(String account, String password) {
        try {
            mImService.register(account, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加连接监听
     *
     * @param l 监听器
     */
    public void addConnectionListener(ConnectionListener l) {
        try {
            mConnectionListener = l;
            if (mImService == null) {
                messageQueue.add(Constant.QUEUE_CONNECTION_LISTENER);
            } else {
                mImService.addConnectionListener();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加聊天监听
     *
     * @param l 监听器
     */
    public void addChatListener(ChatListener l) {
        try {
            mChatListener = l;
            if (mImService == null) {
                messageQueue.add(Constant.QUEUE_CHAT_LISTENER);
            } else {
                mImService.addChatListener();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询聊天记录
     *
     * @param oppositeUserId 想要查询的用户名称 若想查询与abc@hostname的聊天记录，传入abc即可
     * @return 与oppositeUserId相关的所有聊天记录
     */
    public List<ChatRecord> queryChatRecord(String oppositeUserId) {
        if (TextUtils.isEmpty(oppositeUserId)) {
            return null;
        }
        return DBService.getChatRecordDao()
                .queryBuilder()
                .where(ChatRecordDao.Properties.OppositeUserId.eq(oppositeUserId))
                .list();
    }

    private void handleMessageInQueue() throws RemoteException {
        if (messageQueue != null && messageQueue.size() > 0) {
            for (int i = 0; i < messageQueue.size(); i++) {
                String message = messageQueue.get(i);
                switch (message) {
                    case Constant.QUEUE_CREATE_CONNECTION:
                        createConnection();
                        break;
                    case Constant.QUEUE_CONNECTION_LISTENER:
                        mImService.addConnectionListener();
                        break;
                    case Constant.QUEUE_CHAT_LISTENER:
                        mImService.addChatListener();
                        break;
                    case Constant.QUEUE_INIT_CONFIG:
                        initConnectionByConfig();
                        break;
                }
                messageQueue.remove(i);
                i--;
            }
        }
    }

    /**
     * 是否连接上服务器
     *
     * @return 是否连接上服务器
     */
    public boolean isConnected() {
        try {
            return mImService.isConnected();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加好友
     *
     * @param user 用户名 不需要拼服务器名称 若想添加abc@hostname为好友，传入abc即可
     */
    public void addFriend(String user) {
        try {
            mImService.addFriend(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取联系人列表
     *
     * @return 联系人集合
     */
    public List<Contacts> getContactsList() {
        try {
            return mImService.getContactsList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除好友
     * @param user 用户名 不需要拼服务器名称 若想添加abc@hostname为好友，传入abc即可
     */
    public void deleteFriend(String user) {
        try {
            mImService.deleteFriend(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static class LocalConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.ACTION_IM.equals(intent.getAction())) {
                int type = intent.getIntExtra(Constant.BROADCAST_TYPE, -1);
                String message = intent.getStringExtra(IMListenerManager.KEY);
                if (type == Constant.TYPE_CONNECTION_LISTENER) { //监听接口广播
                    IMListenerManager.listener(message, InstantMessaging.with().getConnectionListener());
                } else if (type == Constant.TYPE_RECEIVE_MESSAGE) { //收到消息广播
                    String content = intent.getStringExtra(Constant.CONTENT);
                    String from = intent.getStringExtra(Constant.FROM);
                    String messageType = intent.getStringExtra(Constant.MESSAGE_TYPE);
                    ChatRecord chatRecord = new ChatRecord(null, true, content, from);
                    DBService.getChatRecordDao().insert(chatRecord);
                    IMMessage msg = new IMMessage(content, from, messageType);
                    IMListenerManager.listener(InstantMessaging.with().getChatListener(), msg);
                }
            }
        }
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mImService = IIMService.Stub.asInterface(binder);
            try {
                handleMessageInQueue();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
