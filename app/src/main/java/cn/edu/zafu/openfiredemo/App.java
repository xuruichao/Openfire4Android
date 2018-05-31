package cn.edu.zafu.openfiredemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

import cn.edu.zafu.openfiredemo.im.Constant;
import cn.edu.zafu.openfiredemo.im.InstantMessaging;
import cn.edu.zafu.openfiredemo.im.InstantMessagingConfig;
import cn.edu.zafu.openfiredemo.im.db.DBService;

/**
 * Created by xrc on 18/5/22.
 */

public class App extends Application {

    private static final String serverAdd = "192.168.99.115";
    private static final int port = 5222;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (isMainProcess()) {
            DBService.initDatabase();
            InstantMessagingConfig config = new InstantMessagingConfig.Builder()
                    .server(serverAdd)
                    .port(port)
                    .res(Constant.RES_PHONE)
                    .build();
            InstantMessaging.init().config(config).install();
        }

    }

    private boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfo = am.getRunningAppProcesses();

        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfo) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
