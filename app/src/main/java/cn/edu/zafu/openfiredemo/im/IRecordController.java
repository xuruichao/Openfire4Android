package cn.edu.zafu.openfiredemo.im;

/**
 * IRecordController
 * Created by xrc on 18/5/21.
 */

public interface IRecordController {

    void startRecord();

    void stopRecord();

    void cancelRecord();

    void listenRecord(String filePath);
}
