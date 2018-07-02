package util;

/**
 * @package util
 * @fileName LogUtil
 * @Author Bob on 2018/6/6 14:49.
 * @Describe TODO
 */

public class LogUtil {

    private static LogUtil mLogUtil;

    public final int DEBUG = 0;

    public final int INFO = 1;

    public final int ERROR = 2;

    public final int NOTHING = 3;

    public int level = DEBUG;

    private LogUtil(){}

    public static LogUtil getInstance(){
        if(null == mLogUtil){
            synchronized (LogUtil.class){
                if(null == mLogUtil){
                    mLogUtil = new LogUtil();
                }
            }
        }
        return mLogUtil;
    }

    public void debug(String msg){
        if(DEBUG >= level){
            System.out.println(msg);
        }
    }

    public void info(String msg){
        if(INFO >= level){
            System.out.println(msg);
        }
    }

    public void error(String msg){
        if(ERROR >= level){
            System.out.println(msg);
        }
    }

    public void nothing(String msg){
        if(NOTHING >= level){
            System.out.println(msg);
        }
    }


}
