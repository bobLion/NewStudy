package db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * @package db
 * @fileName DaoSupportFactory
 * @Author Bob on 2018/5/11 7:03.
 * @Describe TODO
 */

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;
    private SQLiteDatabase mSQLiteDatabase;

    // 持有外部数据库的引用
    private DaoSupportFactory (){
        // 把数据库放到内存卡里,判断是否有存储卡  6.0需要动态申请权限
        if (android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            File dbRoot = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "new_study" + File.separator + "database");
            if (!dbRoot.exists()) {
                dbRoot.mkdirs();
            }
            File dbFile = new File(dbRoot, "new_study.db");
            if(!dbFile.exists()){
                try {
                    dbFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 打开或者创建一个数据库
            mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        }
    }

    public static DaoSupportFactory getFactory(){
        if(null == mFactory){
            synchronized (DaoSupportFactory.class){
                if(null == mFactory){
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz){
        IDaoSupport<T> iDaoSupport = new DaoSupport();
        iDaoSupport.init(mSQLiteDatabase,clazz);
        return iDaoSupport;
    }
}
