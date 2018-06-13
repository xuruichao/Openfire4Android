package cn.edu.zafu.openfiredemo.im.audio;

/**
 * 音频管理器
 * Created by xrc on 18/5/21.
 */

public class AudioManager {

    private IRecordController mRecordController;

    public AudioManager() {
        mRecordController = new RecordController();
    }

    public void startRecord() {
        mRecordController.startRecord();
    }

    public void stopRecord() {
        mRecordController.stopRecord();
    }

    public void cancelRecord() {
        mRecordController.cancelRecord();
    }

    public void listenRecord(String filePath) {
        mRecordController.listenRecord(filePath);
    }
}
