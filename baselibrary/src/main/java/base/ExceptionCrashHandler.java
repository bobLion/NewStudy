package base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import config.AppConfig;

/**
 * @package util
 * @fileName ExceptionCrashHandler
 * @Author Bob on 2018/4/13 7:14.
 * @Describe 采用单例的设计模式进行异常捕捉
 * Example File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
 * if(file.exits){
 *  //上传到服务器
 *  try{
*   InputStreamReader fileReader = new InputStreamReader(new FileInputStream(crashFile));
 *  char[] buffer = new char[1024];
 *  int len = 0;
 *  while((len = fileReader.read(buffer))!= -1){
 *  String str = new String(buffer,0,len);
 *  Log.e("TAG",str);
 *  }catch(Exception e){
 *      e.printStackTrace();
 *  }
 *
 *
 *  }
 * }
 *
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {


    private static ExceptionCrashHandler mInstance;

    private Context mContext;

    private static final String  TAG = "ExceptionCrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static ExceptionCrashHandler getInstance(){
        if(null == mInstance){
            synchronized (ExceptionCrashHandler.class){
                if(null == mInstance){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context;
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultExceptionHandler =Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }


    @Override
    public void uncaughtException(Thread thread, Throwable error) {
       /* Log.e(TAG,"程序异常");
        handlerException(error);
        String crashFileName = saveInfoToSD(error);
        cacheCrashFile(crashFileName);
        mDefaultExceptionHandler.uncaughtException(thread,error);*/
        if(!handlerException(error) && null != mDefaultExceptionHandler){
            mDefaultExceptionHandler.uncaughtException(thread,error);
        }else{
            try{
                Thread.sleep(3000);
//                GlobalApplication.getInstance().exit();
            }catch (InterruptedException e){
                Log.e(TAG,"error",e);
            }
            Process.killProcess(Process.myPid());
            System.exit(1);

        }
    }

    private String saveInfoToSD(Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,String> entry: obtainSimpleInfo(mContext).entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        sb.append(obtainExceptionInfo(ex));

        long timeStamp = System.currentTimeMillis();
        String time = formatter.format(new Date());
        fileName = "crash_"+time+ "_"+timeStamp+".log";
        if(Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){
            String path = AppConfig.CRASH_LOG_PATH;
            File dir = new File(path);
           /* File dir = new File(mContext.getFilesDir()+File.separator+"crash"
                    + File.separator);*/
            if(dir.exists()){
                deleteDir(dir);
            }

            if(!dir.exists()){
                dir.mkdir();
            }

            try{
                fileName = dir.toString()
                         + File.separator
                         + getAssignTime("yyyy_MM_dd HH:mm")+".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fileName;
    }

    private String getAssignTime(String dateFormatStr){
        DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dateFormat.format(currentTime);
    }

    /**
     * 获取崩溃文件
     * */
    private File getCrashFile(){
        String crashFileName = mContext.getSharedPreferences("crash",
                Context.MODE_PRIVATE).getString("CRASH_FILE_NAME","");
        return new File(crashFileName);
    }

    /**
     * 缓存崩溃日志文件
     * */
    private void cacheCrashFile(String fileName){
        SharedPreferences sp = mContext.getSharedPreferences("crash",Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME",fileName).commit();
    }

    /**
     *
     * 获取一些简单信息，如设备版本信息，软件版本信息，型号等信息
     * */
    private HashMap<String,String> obtainSimpleInfo(Context context){
        HashMap<String,String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try{
            packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(),PackageManager.GET_ACTIVITIES);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        map.put("versionName",packageInfo.versionName);
        map.put("versionCode","" + packageInfo.versionCode);
        map.put("MODEL", Build.MODEL);
        map.put("SDK_INT","" + Build.VERSION.SDK_INT);
        map.put("PRODUCT",""+ Build.PRODUCT);
        map.put("MODLE_INFO",getMobileInfo());
        return map;
    }

    /**
     * 获取手机信息
     * */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try{
            Field[] fields = Build.class.getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "="+value);
                sb.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取系统未捕捉到的异常信息
     * */
    private String obtainExceptionInfo(Throwable throwable){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * 删除文件夹下面的文件
     * */
    private boolean deleteDir(File dir){
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for(File file:files){
                file.delete();
            }
        }
        return true;
    }

    private boolean handlerException(Throwable ex){
        if(ex == null){
            return false;
        }

        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext,"很抱歉，程序异常即将退出", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        saveInfoToSD(ex);
//        collectDevInfo(mContext);
//        saveCrashInfo2File(ex);
        return true;
    }
}
