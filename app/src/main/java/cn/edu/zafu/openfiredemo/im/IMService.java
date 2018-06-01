package cn.edu.zafu.openfiredemo.im;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.zafu.openfiredemo.im.bean.Contacts;
import cn.edu.zafu.openfiredemo.im.bean.IMMessage;


/**
 * 即时通讯服务
 * Created by xrc on 18/5/21.
 */

public class IMService extends Service {

    private boolean mBroadcastEnable = false;

    private boolean mConnectedListenerAdded = false;

    private boolean mChatListenerAdded = false;

    private AudioManager audioManager;

    public static XMPPTCPConnection mConnection;

    private InstantMessagingConfig mInstantMessagingConfig;

    private IMHandler mImHandler = new IMHandler();

    private IBinder mBinder = new IIMService.Stub() {

        @Override
        public void autoReceipt() throws RemoteException {
            IMService.this.autoReceipt();
        }

        @Override
        public void autoFriend(boolean autoFriend) throws RemoteException {
            IMService.this.autoFriend(autoFriend);
        }

        @Override
        public void createConnection() throws RemoteException {
            IMService.this.createConnection();
        }

        @Override
        public void login(String account, String password) throws RemoteException {
            IMService.this.login(account, password);
        }

        @Override
        public void register(String account, String password) throws RemoteException {
            IMService.this.register(account, password);
        }

        @Override
        public void startRecord() throws RemoteException {
            audioManager.startRecord();
        }

        @Override
        public void stopRecord() throws RemoteException {
            audioManager.stopRecord();
        }

        @Override
        public void cancelRecord() throws RemoteException {
            audioManager.cancelRecord();
        }

        @Override
        public void listenRecord(String filePath) throws RemoteException {
            audioManager.listenRecord(filePath);
        }

        @Override
        public void sendMessage(String targetAddress, String content) throws RemoteException {
            IMService.this.sendMessage(targetAddress, content);
        }

        @Override
        public void addFriend(String name) throws RemoteException {
            IMService.this.addFriend(name);
        }

        @Override
        public void deleteFriend(String name) throws RemoteException {
            IMService.this.deleteFriend(name);
        }

        @Override
        public List<Contacts> getContactsList() throws RemoteException {
            return IMService.this.getFriendsList();
        }

        @Override
        public void addConnectionListener() throws RemoteException {
            IMService.this.addConnectionListener();
        }

        @Override
        public void addChatListener() throws RemoteException {
            IMService.this.addChatListener();
        }

        @Override
        public void disconnect() throws RemoteException {
            IMService.this.disconnect();
        }

        @Override
        public boolean isConnected() throws RemoteException {
            return IMService.this.isConnected();
        }

    };

    private void deleteFriend(String name) {
        Roster roster = Roster.getInstanceFor(mConnection);
        RosterEntry entry = roster.getEntry(name + "@" + mInstantMessagingConfig.getServer());
        try {
            roster.removeEntry(entry);
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private void autoFriend(final boolean autoFriend) {
        final AndFilter filter = new AndFilter(new StanzaTypeFilter(Presence.class));
        mConnection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                if (packet instanceof Presence) {
                    Presence presence = (Presence) packet;
                    String from = presence.getFrom();
                    String fromUser = from.split("@")[0];
                    if (presence.getType().equals(Presence.Type.subscribe)) {//对方请求订阅
                        if (autoFriend) {
                            Presence pres = new Presence(Presence.Type.subscribed);
                            pres.setTo(from);
                            try {
                                mConnection.sendStanza(pres);
                                addFriend(fromUser);
                            } catch (SmackException.NotConnectedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (presence.getType().equals(Presence.Type.subscribed)) {//对方同意订阅
                        Log.e("TAG", "同意订阅" + from);
                    } else if (presence.getType().equals(Presence.Type.unsubscribe)) {//对方取消订阅
                        Log.e("TAG", "取消订阅" + from);
                    } else if (presence.getType().equals(Presence.Type.unsubscribed)) {//对方拒绝订阅
                        Log.e("TAG", "拒绝订阅" + from);
                    } else if (presence.getType().equals(Presence.Type.unavailable)) {//离线
                       // Log.e("TAG", "离线" + from);
                    } else if (presence.getType().equals(Presence.Type.available)) {//上线
                       // Log.e("TAG", "上线" + from);
                    }
                }
            }
        }, filter);
    }

    private void autoReceipt() {
        ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
        ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());
        DeliveryReceiptManager.getInstanceFor(mConnection).autoAddDeliveryReceiptRequests();
    }

    private List<Contacts> getFriendsList() {
        Roster instanceFor = Roster.getInstanceFor(mConnection);
        Set<RosterEntry> entries = instanceFor.getEntries();
        List<Contacts> list = new ArrayList<>(entries.size());
        for (RosterEntry entry : entries) {
            Contacts contacts = new Contacts();
            contacts.setUser(entry.getUser());
            contacts.setName(entry.getName());
            contacts.setType(entry.getType().toString());
            list.add(contacts);
        }
        return list;
    }

    private void addFriend(String name) {
        Roster roster = Roster.getInstanceFor(mConnection);
        try {
            roster.createEntry(name.trim() + "@" + mInstantMessagingConfig.getServer(), name, new String[]{"Friends"});
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private void createConnection() {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(mInstantMessagingConfig.getServer());
        builder.setHost(mInstantMessagingConfig.getServer());
        builder.setPort(mInstantMessagingConfig.getPort());
        builder.setResource(mInstantMessagingConfig.getRes());
        builder.setCompressionEnabled(false);
        builder.setDebuggerEnabled(true);
        builder.setSendPresence(false);
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        mConnection = new XMPPTCPConnection(builder.build());
    }

    private void login(final String account, final String password) {
        mInstantMessagingConfig.setTempUserName(account);
        mInstantMessagingConfig.setTempPassword(password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mConnection.connect();
                    mConnection.login(account, password);
                    Presence presence = new Presence(Presence.Type.available);
                    presence.setStatus("我是在线状态");
                    presence.setPriority(1);
                    mConnection.sendStanza(presence);
                    ChatManager chatmanager = ChatManager.getInstanceFor(mConnection);
                    chatmanager.addChatListener(new ChatManagerListener() {
                        @Override
                        public void chatCreated(Chat chat, boolean createdLocally) {
                            chat.addMessageListener(new ChatMessageListener() {
                                @Override
                                public void processMessage(Chat chat, Message message) {
                                    String content = message.getBody();
                                    if (content != null) {
                                        IMMessage msg = new IMMessage(message.getBody(), message.getFrom(), IMMessageExtension.Type.PIC);
                                        if (mBroadcastEnable && mChatListenerAdded) {
                                            sendMessageBroadCast(msg);
                                        }
                                    }
                                }
                            });
                        }
                    });
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void register(String account, String password) {
        try {
            AccountManager.getInstance(mConnection).createAccount(account, password);
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String targetAddress, String content) {
        try {
            Message message = new Message();
            message.setBody(content);
            IMMessageExtension imMessageExtension = new IMMessageExtension();
            imMessageExtension.setType(IMMessageExtension.Type.TEXT);
            message.addExtension(imMessageExtension);
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            Chat chat = chatManager.createChat(targetAddress);
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        mConnection.disconnect();
    }

    private boolean isConnected() {
        return mConnection.isConnected();
    }

    private void addChatListener() {
        if (mChatListenerAdded) {
            return;
        }
        mBroadcastEnable = true;
        mChatListenerAdded = true;
    }

    private void addConnectionListener() {
        if (mConnectedListenerAdded) {
            return;
        }
        mConnection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {
                if (mBroadcastEnable) {
                    IMService.this.sendListenerBroadcast(IMListenerManager.CONNECTION_CONNECTED);
                }
            }

            @Override
            public void authenticated(XMPPConnection connection, boolean resumed) {
                mInstantMessagingConfig.saveUserInfo();
                if (mBroadcastEnable) {
                    IMService.this.sendListenerBroadcast(IMListenerManager.CONNECTION_AUTHENTICATED);
                }
            }

            @Override
            public void connectionClosed() {
                if (mBroadcastEnable) {
                    IMService.this.sendListenerBroadcast(IMListenerManager.CONNECTION_CONNECTION_CLOSED);
                }
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                if (mBroadcastEnable) {
                    mImHandler.postDelayed(new ConnectedRunnable(), 6000);
                    IMService.this.sendListenerBroadcast(IMListenerManager.CONNECTION_CLOSED_ON_ERROR);
                }
            }

            @Override
            public void reconnectionSuccessful() {
                if (mBroadcastEnable) {
                    IMService.this.sendListenerBroadcast(IMListenerManager.RECONNECTION_SUCCESSFUL);
                }
            }

            @Override
            public void reconnectingIn(int seconds) {
                if (mBroadcastEnable) {
                    IMService.this.sendListenerBroadcast(IMListenerManager.RECONNECTING_IN);
                }
            }

            @Override
            public void reconnectionFailed(Exception e) {
                if (mBroadcastEnable) {
                    IMService.this.sendListenerBroadcast(IMListenerManager.RECONNECTION_FAILED);
                }
            }
        });
        mBroadcastEnable = true;
        mConnectedListenerAdded = true;
    }

    private void sendListenerBroadcast(String info) {
        Intent intent = new Intent(Constant.ACTION_IM);
        intent.putExtra(Constant.BROADCAST_TYPE, Constant.TYPE_CONNECTION_LISTENER);
        intent.putExtra(IMListenerManager.KEY, info);
        sendBroadcast(intent);
    }

    private void sendMessageBroadCast(IMMessage message) {
        Intent intent = new Intent(Constant.ACTION_IM);
        intent.putExtra(Constant.BROADCAST_TYPE, Constant.TYPE_RECEIVE_MESSAGE);
        intent.putExtra(Constant.CONTENT, message.getContent());
        String from = message.getFrom().substring(0, message.getFrom().lastIndexOf("/"));
        intent.putExtra(Constant.FROM, from);
        intent.putExtra(Constant.MESSAGE_TYPE, message.getType());
        sendBroadcast(intent);
    }

    public void reConnect() {
        login(mInstantMessagingConfig.getUserName(), mInstantMessagingConfig.getPassword());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        audioManager = new AudioManager();
        mInstantMessagingConfig = intent.getParcelableExtra(Constant.CONNECTION_CONFIG);
        return mBinder;
    }

    private class ConnectedRunnable implements Runnable {

        @Override
        public void run() {
            IMService.this.reConnect();
        }
    }

}
