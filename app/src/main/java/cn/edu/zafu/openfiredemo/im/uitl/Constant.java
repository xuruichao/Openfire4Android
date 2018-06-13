package cn.edu.zafu.openfiredemo.im.uitl;

import android.os.Environment;

/**
 * 常量类
 * Created by xrc on 18/5/22.
 */

public class Constant {

    private static final String RECORD_FILE_NAME = "qd_record.amr";

    public static final String RES_PHONE = "Phone";

    public static final String RES_PAD = "Pad";

    public static final String QUEUE_CONNECTION_LISTENER = "queue_connection_listener";

    public static final String QUEUE_CHAT_LISTENER = "queue_chat_listener";

    public static final String QUEUE_CREATE_CONNECTION = "queue_create_connection";

    public static final String QUEUE_INIT_CONFIG = "queue_init_config";

    public static final String RECORD_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SoundRecorder";

    public static final String RECORD_FILE_PATH = RECORD_FILE_DIR + "/" + RECORD_FILE_NAME;

    public static final String CONNECTION_CONFIG = "connection_config";

    public static final String ACTION_IM = "cn.edu.zafu.openfiredemo.IM";

    public static final String BROADCAST_TYPE = "broadcast_type";

    public static final String MESSAGE_TYPE = "message_type";

    public static final String CONTENT = "content";

    public static final String FROM = "from";

    public static final int TYPE_CONNECTION_LISTENER = 1;

    public static final int TYPE_RECEIVE_MESSAGE = 2;

}
