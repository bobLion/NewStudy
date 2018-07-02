package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @package db
 * @fileName DaoSupport
 * @Author Bob on 2018/5/11 7:01.
 * @Describe TODO
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DaoSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClazz;
    private String TAG =  "DaoSupport";
    private QuerySupport<T> mQuerySupport;

    private static final Object[] mPutMethodArgs = new Object[2];
    private static final Map<String,Method> mPutMethods = new ArrayMap<>();

    public void init(SQLiteDatabase sqliteDatabase,Class<T> clazz){
        this.mSQLiteDatabase = sqliteDatabase;
        this.mClazz = clazz;

//        创建表
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))
                .append(" (id integer primary key autoincrement,");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();
            sql.append(name).append(DaoUtil.getColumnType(type)).append(", ");
            // 对数据类型进行转换 int --->Integer  ,String-->text,
        }
        sql.replace(sql.length()-2,sql.length(),")");
        String createTableSql = sql.toString();
        Log.e(TAG,"创建表语句： "+createTableSql);
        mSQLiteDatabase.execSQL(createTableSql);
    }

    // 插入数据库
    @Override
    public long insert(T obj) {
//        mSQLiteDatabase.insert()
        ContentValues values  = cotentValueByObj(obj);
        return mSQLiteDatabase.insert(DaoUtil.getTableName(mClazz),null,values);
    }

    @Override
    public void insert(List<T> datas) {
        mSQLiteDatabase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    @Override
    public QuerySupport<T> querySupport() {
        if(null == mQuerySupport){
            mQuerySupport = new QuerySupport<>(mSQLiteDatabase,mClazz);
        }
        return mQuerySupport;
    }


    /**
     * 删除
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int delete(String whereClause,String[] whereArgs){
        return mSQLiteDatabase.delete(DaoUtil.getTableName(mClazz),whereClause,whereArgs);
    }

    /**
     * 更新
     * @param obj
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int update(T obj,String whereClause,String ... whereArgs){
        ContentValues values = cotentValueByObj(obj);
        return mSQLiteDatabase.update(DaoUtil.getTableName(mClazz),values,whereClause,whereArgs);
    }

    /*private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if(null != cursor && cursor.moveToFirst()){
            // 从游标里面获取数据
            do {
                try {
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();
                        // 获取脚标 获取在第几列
                        int index = cursor.getColumnIndex(name);
                        if(index == -1){
                            continue;
                        }
                        // 通过反射获取游标
                        Method cursorMethod = cursorMethod(field.getType());
                        if(null != cursorMethod){
                            Object value = cursorMethod.invoke(cursor,index);
                            if(null == value){
                                continue;
                            }

                            // 处理一些特殊的部分
                            if(field.getType() == boolean.class || field.getType() == Boolean.class){
                                if("0".equals(String.valueOf(value))){
                                    value = false;
                                }else if("1".equals(String.valueOf(value))){
                                    value = true;
                                }
                            }else if(field.getType() == char.class || field.getType() == Character.class){
                                value = ((String)value).charAt(0);
                            }else if(field.getType() == Date.class){
                                long date = (Long) value;
                                if(date <= 0){
                                    value = null;
                                }else{
                                    value = new Date(date);
                                }
                            }
                            field.set(instance,value);
                        }
                    }

                    list.add(instance);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }*/

    /*private Method cursorMethod(Class<?> type) throws NoSuchMethodException {
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName,int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> type) {
        String typeName;
        if(type.isPrimitive()){
            typeName = DaoUtil.captialize(type.getName());
        }else{
            typeName = type.getSimpleName();
        }
        String methodName = "get" + typeName;
        if("getBollean".equals(methodName)){
            methodName = "getInt";
        }else if("getChar".equals(methodName) || "getCharacter".equals(methodName)){
            methodName = "getString";
        }else if("getDate".equals(methodName)){
            methodName = "getLong";
        }else if("getInteger".equals(methodName)){
            methodName = "getInt";
        }
        return methodName;
    }
*/
    // object 转成contentValue
    private ContentValues cotentValueByObj(T obj) {
        ContentValues values = new ContentValues();
        //封装value
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                // put 第二个参数是类型， 把它转换
                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;
                String fieldTypeName = field.getType().getName();
                Method putMethod = mPutMethods.get(fieldTypeName);
                if(null == putMethod){
                    putMethod = ContentValues.class.getDeclaredMethod("put",
                            String.class,value.getClass());
                    mPutMethods.put(fieldTypeName,putMethod);
                }
                // 通过反射执行
                putMethod.invoke(values,mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs [1] = null;
            }
        }
        return values;
    }
}
