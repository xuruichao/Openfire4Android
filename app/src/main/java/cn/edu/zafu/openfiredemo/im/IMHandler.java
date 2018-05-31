package cn.edu.zafu.openfiredemo.im;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import cn.edu.zafu.openfiredemo.App;

/**
 * 用来观察
 * Created by xrc on 18/5/22.
 */

public class IMHandler extends Handler {

    public static final int MESSAGE_RECONNECTED = 20;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_RECONNECTED:

                break;
        }
    }
}
