package http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @package http
 * @fileName HttpUtils
 * @Author Bob on 2018/4/22 21:34.
 * @Describe 实现http请求
 */

public class HttpUtils{

    private String mUrl;

    // 请求方式
    private int mType = GET_TYPE;
    private static final int POST_TYPE = 0X0011;
    private static final int GET_TYPE = 0X0022;

    private Map<String,Object> mParams;

    private Context mContext;

    // 是否读取缓存
    private boolean mCache = false;


    public HttpUtils(Context context){
        this.mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    /**
     * 添加请求路径
     * @param url
     * @return
     */
    public HttpUtils url(String url){
        this.mUrl = url;
        return this;
    }
    public HttpUtils post(){
        mType = POST_TYPE;
        return this;
    }

    /**
     * 请求的方式
     * @return
     */
    public HttpUtils get(){
        mType = GET_TYPE;
        return this;
    }

    // 是否配置缓存
    public HttpUtils cache(boolean isCache){
        mCache = isCache;
        return this;
    }

    /**
     * 添加参数
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParam(String key,Object value){
        mParams.put(key,value);
        return this;
    }

    public HttpUtils addParams(Map<String ,Object> params){
        mParams.putAll(params);
        return this;
    }

    /**
     * 添加回调 执行
     * @param callBack
     * @return
     */
    public void execute(EngineCallBack callBack){
        if(null == callBack){
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }

        callBack.onPreExecute(mContext,mParams);
        //判断执行方法
        if(mType == POST_TYPE){
            post(mUrl,mParams,callBack);
        }

        if(mType == GET_TYPE){
            get(mUrl,mParams,callBack);
        }
    }


    public void execute(){
        execute(null);
    }

    // 默认OKHttpEngine
    private static IHttpEngine mHttpEngine = null;
//    private static IHttpEngine mHttpEngine = new OKHttpEngine();

    /**
     * 在application里面初始化引擎
     * @param httpEngine
     */
    public static void init(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
    }

    /**
     * 可传入自带引擎
     * @param httpEngine
     */
    public HttpUtils exchangeEngine(IHttpEngine httpEngine){
        mHttpEngine = httpEngine;
        return this;
    }

    private  void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mCache,mContext,url,params,callBack);
    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mCache,mContext,url,params,callBack);
    }

    /**
     * 拼接参数
     * @param url
     * @param params
     * @return
     */
    public static String joinParams(String url ,Map<String,Object> params){
        if(null == params || params.size() <= 0 ){
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if(!url.contains("?")){
            stringBuffer.append("?");
        }else{
            if(!url.endsWith("?")){
                stringBuffer.append("&");
            }
        }

        for(Map.Entry<String,Object> entry:params.entrySet()){
            stringBuffer.append(entry.getKey() + "-" + entry.getValue() + "&");
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }
    /**
     * 解析一个类的class信息
     * @param object
     * @return
     */
    public static Class<?> analysisClazzInfo(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] parmas = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) parmas[0];
    }
}
