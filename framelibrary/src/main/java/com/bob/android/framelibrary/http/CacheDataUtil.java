package com.bob.android.framelibrary.http;

import java.util.List;

import db.DaoSupportFactory;
import db.IDaoSupport;
import util.MD5Util;

/**
 * @package com.bob.android.framelibrary.http
 * @fileName CacheDataUtil
 * @Author Bob on 2018/5/18 16:08.
 * @Describe TODO
 */

public class CacheDataUtil {

    /**
     * 获取本地数据库的缓存数据
     * @param finalUrl
     * @return
     */
    public static String getCacheResultJson(final String finalUrl ){
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory
                .getFactory().getDao(CacheData.class);
        List<CacheData> cacheDatas = dataDaoSupport.querySupport()
                .selection("mUrlKey=?").selectionArgs(MD5Util.string2MD5(finalUrl)).query();
        if(null != cacheDatas && cacheDatas.size() != 0) {
            CacheData cacheData = cacheDatas.get(0);
            String resultJson = cacheData.getmResultJson();
            return resultJson;
        }
        return null;
    }

    /**
     * 把数据缓存到本地数据库里
     * @param finalUrl
     * @param resultJson
     * @return
     */
    public static long cacheData(String finalUrl,String resultJson){
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory
                .getFactory().getDao(CacheData.class);
        dataDaoSupport.delete("mUrlKey=?",MD5Util.string2MD5(finalUrl));
        long number = dataDaoSupport.insert(new CacheData(MD5Util.string2MD5(finalUrl),resultJson));
        return number;
    }
}
