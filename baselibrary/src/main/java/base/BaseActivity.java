package base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ioc.ViewUtils;

/**
 * Created by Bob on 2018/4/12.
 */

public abstract class BaseActivity extends AppCompatActivity{

    public Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        mContext = this;
        ViewUtils.inject(this);
        initView();
        initVariables();
        initTitle();
        initData();
    }

    protected abstract void initVariables();

    /**
     * 设置布局
     * **/
    protected abstract void  setContentView();

    /**
     * 初始化头部
     * */
    protected abstract void  initTitle();

    /**
     * 初始化界面
     * */
    protected abstract void  initView();

    /**
     * 初始化数据
     * */
    protected abstract void  initData();

    /**
     * 启动 activity
     * @param clazz
     * */
    protected  void startActivity(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    /**
     * findViewById
     * @return View
     * */
    protected  <T extends View> T viewById(int viewId){
        return (T)findViewById(viewId);
    }



}
