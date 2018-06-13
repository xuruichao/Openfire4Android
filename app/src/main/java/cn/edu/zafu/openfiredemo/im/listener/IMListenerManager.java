package cn.edu.zafu.openfiredemo.im.listener;

import cn.edu.zafu.openfiredemo.im.extension.IMMessageExtension;
import cn.edu.zafu.openfiredemo.im.bean.IMMessage;

/**
 * 即时通讯监听管理类
 * Created by xrc on 18/5/23.
 */

public class IMListenerManager {

    public static final String KEY = "IMListenerManageKey";

    /**
     * 连接服务器成功
     */
    public static final String CONNECTION_CONNECTED = "connection_connected";

    /**
     * 服务器校验成功
     */
    public static final String CONNECTION_AUTHENTICATED = "connection_authenticated";

    /**
     * 连接关闭
     */
    public static final String CONNECTION_CONNECTION_CLOSED = "connection_connection_closed";

    /**
     * 连接因为错误关闭(可能是这个意思)
     */
    public static final String CONNECTION_CLOSED_ON_ERROR = "connection_closed_on_error";

    /**
     * 重连成功
     */
    public static final String RECONNECTION_SUCCESSFUL = "reconnection_successful";

    /**
     * 重新连接进来
     */
    public static final String RECONNECTING_IN = "reconnecting_in";

    /**
     * 重连失败
     */
    public static final String RECONNECTION_FAILED = "reconnection_failed";

    /**
     * 连接异常，可能由各种原因导致，超时，验证错误等等
     */
    public static final String CONNECTION_EXCEPTION = "connection_exception";

    public static void listener(String message, ConnectionListener l) {
        switch (message) {
            case CONNECTION_CONNECTED:
                if (l != null) {
                    l.connected();
                }
                break;
            case CONNECTION_AUTHENTICATED:
                if (l != null) {
                    l.authenticated();
                }
                break;
            case CONNECTION_CONNECTION_CLOSED:
                if (l != null) {
                    l.connectionClosed();
                }
                break;
            case CONNECTION_CLOSED_ON_ERROR:
                if (l != null) {
                    l.connectionClosedOnError();
                }
                break;
            case RECONNECTION_SUCCESSFUL:
                if (l != null) {
                    l.reconnectionSuccessful();
                }
                break;
            case RECONNECTING_IN:
                if (l != null) {
                    l.reconnectingIn();
                }
                break;
            case RECONNECTION_FAILED:
                if (l != null) {
                    l.reconnectionFailed();
                }
                break;
            case CONNECTION_EXCEPTION:
                if (l != null) {
                    l.connectionException();
                }
                break;
        }
    }

    public static void listener(ChatListener l, IMMessage message) {
        switch (message.getType()) {
            case IMMessageExtension.Type.TEXT:
                if (l != null) {
                    l.onReceiveText(message);
                }
                break;
            case IMMessageExtension.Type.AUDIO:
                if (l != null) {
                    l.onReceiveAudio(message);
                }
                break;
            case IMMessageExtension.Type.PIC:
                if (l != null) {
                    l.onReceivePic(message);
                }
                break;
        }
    }
}
