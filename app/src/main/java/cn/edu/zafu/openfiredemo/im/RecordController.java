package cn.edu.zafu.openfiredemo.im;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * RecordController
 * Created by xrc on 18/5/21.
 */

public class RecordController implements IRecordController {

    private static final String TAG = RecordController.class.getSimpleName();

    private String mFilePath;
    private MediaRecorder mRecorder;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void startRecord() {
        File file = new File(Constant.RECORD_FILE_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        mFilePath = Constant.RECORD_FILE_PATH;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);

        try {
            mRecorder.prepare();
            mRecorder.start();
            //long mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "录音失败");
        }
    }

    @Override
    public void stopRecord() {
        mRecorder.stop();
        //long mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();

        mRecorder = null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void cancelRecord() {
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        File file = new File(mFilePath);
        if (!TextUtils.isEmpty(mFilePath) && file.exists()) {
            file.delete();
        }
        mFilePath = null;
    }

    @Override
    public void listenRecord(String filePath) {
        final MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
