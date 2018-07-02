package com.bob.android.framelibrary.http;

/**
 * @package com.bob.android.framelibrary.http
 * @fileName CacheData
 * @Author Bob on 2018/5/18 6:31.
 * @Describe 缓存的实体类
 */

public class CacheData {
    // 请求的链接
    private String mUrlKey;
    // 后台返回的json
    private String mResultJson;

    public CacheData() {
    }

    public CacheData(String mUrlKey, String mResultJson) {
        this.mUrlKey = mUrlKey;
        this.mResultJson = mResultJson;
    }

    public String getmUrlKey() {
        return mUrlKey;
    }

    public void setmUrlKey(String mUrlKey) {
        this.mUrlKey = mUrlKey;
    }

    public String getmResultJson() {
        return mResultJson;
    }

    public void setmResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }
}
