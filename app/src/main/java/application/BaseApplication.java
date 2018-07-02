package application;

import android.app.Application;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.bob.android.framelibrary.http.OKHttpEngine;

import base.ExceptionCrashHandler;
import fixbug.FixDexManager;
import http.HttpUtils;
import skin.SkinManager;

/**
 * @package application
 * @fileName BaseApplication
 * @Author Bob on 2018/4/14 20:41.
 * @Describe TODO
 */

public class BaseApplication extends Application {

    public static PatchManager mPatchManager;

    public FixDexManager mFixDexManager;
    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionCrashHandler.getInstance().init(this);
        HttpUtils.init(new OKHttpEngine());
        SkinManager.getInstance().init(this);
       /* //初始化阿里热修复
        mPatchManager = new PatchManager(this);
        //初始化版本，获取当前应用版本
        try {
            String appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
            mPatchManager.init(appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //加载之前的apatch包
        mPatchManager.loadPatch();*/

        try {
            mFixDexManager = new FixDexManager(this);
            mFixDexManager.loadDexFix();//加载全部的修复包
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
