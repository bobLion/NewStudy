package http;


import util.StringUtils;

/**
 * Created by sailing on 2016/6/20.
 */
public class ResponseCode {
    /**
     * 请求成功
     */
    public static final int RESPONSE_CODE_SUCCESS = 1000;

    /**
     * 请求失败
     */
    public static final int RESPONSE_CODE_FAILURE = 1001;

    /**
     * @Field RESPONSE_CODE_NULL :返回结果为null
     */
    public static final int RESPONSE_CODE_NODATA = 1010;

    /**
     * @Field RESPONSE_CODE_NULL :查无证
     */
    public static final int RESPONSE_NO_CODE = 1008;

    /**
     * @Field RESPONSE_CODE_TIMEOUT :请求超时
     */
    public static final int RESPONSE_CODE_TIMEOUT = 1004;

    /**
     * @Field RESPONSE_CODE_FAILD :MD5校验码失败
     */
    public static final int RESPONSE_CODE_UNKNOWN = 9997;

    public static final int RESPONSE_CHECK_ERROR = 9998;

    public static final int RESPONSE_CODE_FAILD = 9999;

    private int responseCode;
    private String content;
    private String checkCode;
    private String key;
    private String message;
    private String responseResult;

    public ResponseCode() {
    }

    public String getResponseResult() {
        return this.responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getContent() {
        return !StringUtils.isBlank(this.content)?this.content:this.responseResult;
    }

    public void setContent(String responseResult) {
        this.content = responseResult;
    }

    public String getCheckCode() {
        return this.checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
