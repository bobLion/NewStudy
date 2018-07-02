package http;

import android.content.Context;

import java.util.Map;

/**
 * @package http
 * @fileName EngineCallBack
 * @Author Bob on 2018/4/22 21:38.
 * @Describe TODO
 */

public interface EngineCallBack {

    /**
     * 开始执行之前回调的方法
     * @param context
     * @param parms
     */
    public void onPreExecute(Context context, Map<String,Object> parms);
    /**
     * 请求错误
     * @param e
     */
    public void onError(Exception e);



    /**
     * 请求成功
     * @param result
     */
    public void onSuccess(String result);

    /**
     * 默认的
     */
    public final EngineCallBack DEFAULT_CALL_BACK  = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> parms) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
