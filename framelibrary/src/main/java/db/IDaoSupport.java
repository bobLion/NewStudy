package db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * @package db
 * @fileName IDaoSupport
 * @Author Bob on 2018/5/11 6:59.
 * @Describe TODO
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqliteDatabase, Class<T> clazz);
    // 插入数据
    public long insert(T t);

    // 检测性能
    public void insert(List<T> data );

    // 获取专门查询的支持类
    QuerySupport<T> querySupport();

    // 删除
    int delete(String whereClause,String... whereArgs);

    // 更新
    int update(T obj,String whereClause,String ... whereArgs);


}
