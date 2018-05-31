package cn.edu.zafu.openfiredemo.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

import cn.edu.zafu.openfiredemo.greendao.ChatRecordDao;
import cn.edu.zafu.openfiredemo.greendao.DaoMaster;

/**
 * Created by lvhongzhen on 2018/3/21.
 */

public class SqlLiteOpenHelper extends DaoMaster.OpenHelper {
    public SqlLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, ChatRecordDao.class);
    }
}
