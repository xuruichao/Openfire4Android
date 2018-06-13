package cn.edu.zafu.openfiredemo.im.listener;

/**
 * 监听连接接口
 * Created by xrc on 18/5/23.
 */

public interface ConnectionListener {

    void connected();

    void authenticated();

    void connectionClosed();

    void connectionClosedOnError();

    void reconnectionSuccessful();

    void reconnectingIn();

    void reconnectionFailed();

    void connectionException();
}
