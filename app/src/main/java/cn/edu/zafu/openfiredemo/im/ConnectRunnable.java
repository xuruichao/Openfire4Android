package cn.edu.zafu.openfiredemo.im;

/**
 * ConnectRunnable
 * Created by xrc on 18/5/31.
 */

public class ConnectRunnable implements Runnable {

    private IMService service;

    public ConnectRunnable(IMService service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.reConnect();
    }
}
