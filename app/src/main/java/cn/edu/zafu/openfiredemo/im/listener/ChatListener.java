package cn.edu.zafu.openfiredemo.im.listener;

import cn.edu.zafu.openfiredemo.im.bean.IMMessage;

/**
 * 监听聊天消息接口
 * Created by xrc on 18/5/23.
 */

public interface ChatListener {

    void onReceiveText(IMMessage message);

    void onReceiveAudio(IMMessage message);

    void onReceivePic(IMMessage message);

}
