package http;

import java.io.Serializable;

import util.StringUtils;

/**
 * @package http
 * @fileName ResponseResult
 * @Author Bob on 2018/5/31 10:10.
 * @Describe TODO
 */

public class ResponseResult implements Serializable {

    public static final int RESPONSE_CODE_SUCCESS = 1000;

    public static  final int SERVER_FAILURE = 9999;

    public static final int VALIDATE_ERROR = 2001;

    public static final int EMPTY_DATA =  4001;


    private int responseCode;
    private String content;
    private String message;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
