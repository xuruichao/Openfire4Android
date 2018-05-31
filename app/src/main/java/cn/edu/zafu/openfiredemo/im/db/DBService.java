package cn.edu.zafu.openfiredemo.im.db;

import android.database.sqlite.SQLiteDatabase;

import cn.edu.zafu.openfiredemo.App;
import cn.edu.zafu.openfiredemo.greendao.ChatRecordDao;
import cn.edu.zafu.openfiredemo.greendao.DaoMaster;
import cn.edu.zafu.openfiredemo.greendao.DaoSession;


/**
 * Created by lvhongzhen on 2018/3/21.
 */

public class DBService {
    private static final String DB_NAME = "QudaoIMDB";
    private static DaoSession mDaoSession;

    /**
     * 初始化greenDao，这个操作建议在Application初始化的时候添加；
     */
    public static void initDatabase() {
        SqlLiteOpenHelper mHelper = new SqlLiteOpenHelper(App.getInstance(), DB_NAME, null);
        SQLiteDatabase mDb = mHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();

    }

    public static ChatRecordDao getChatRecordDao() {
        return mDaoSession.getChatRecordDao();
    }


}