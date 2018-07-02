package http;

import android.content.Context;

import java.util.Map;

/**
 * @package http
 * @fileName IHttpEngine
 * @Author Bob on 2018/4/22 21:35.
 * @Describe http引擎的规范
 */

public interface IHttpEngine {

    // get 请求
    void get(boolean cache,Context context,String url, Map<String,Object> params, EngineCallBack callBack);

    // post 请求
    void post(boolean cache,Context context ,String url, Map<String,Object> params,EngineCallBack callBack);

    // 下载文件


    // 上传文件


    // http添加证书


}
