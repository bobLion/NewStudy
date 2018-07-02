package com.bob.android.framelibrary.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import skin.SkinManager;
import skin.attr.SkinAttr;
import skin.attr.SkinView;
import skin.support.SkinAppCompatViewInflater;
import skin.support.SkinAttrSupport;

/**
 * Created by Bob on 2018/4/12.
 */

public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory {

    private static final String TAG = "TAG" ;
    private SkinAppCompatViewInflater mSkinAppCompatViewInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater,this);
        /*LayoutInflaterCompat.setFactory(layoutInflater, new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                  if (name.equals("Button")) {
            TextView tv = new TextView(BaseSkinActivity.this);
            tv.setText("按钮变成了textView");
            tv.setTextColor(getResources().getColor(android.support.compat.R.color.notification_icon_bg_color));
            return tv;
        });*/
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createView(parent,name,context,attrs);
        //拦截到view的创建 获取到View之后要去解析
               // Log.e("TAG", "“拦截到View的创建");


        // 1 创建view

        // 2 解析属性 src textColor background 自定义属性

        // 3 统一交给skingManager处理
        Log.e(TAG,view+"");
        if(null != view){
            List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttrs(context,attrs);
            SkinView skinView = new SkinView(view,skinAttrList);
            // 统一管理skinView
            managerSkinView(skinView);

            // 判断一下要不要换肤
            SkinManager.getInstance().checkChangeSkin(skinView);
        }



        return view;
    }

    /**
     * 统一管理skinView
     * @param skinView
     */
    private  void managerSkinView(SkinView skinView){
        List<SkinView> skinViewList = SkinManager.getInstance().getSkinViews(this);
        if(null == skinViewList){
            skinViewList = new ArrayList<>();
            SkinManager.getInstance().register(this,skinViewList);
        }
        skinViewList.add(skinView);
    }

    public View createView(View parent, final String name, @NonNull Context context,
                             @NonNull AttributeSet attrs){
        final boolean isPre2 = Build.VERSION.SDK_INT < 21;
        if(null == mSkinAppCompatViewInflater){
            mSkinAppCompatViewInflater = new SkinAppCompatViewInflater();
        }
        final boolean inHeritContext = isPre2 && true
                && shouldInheritContext((ViewParent)parent);
        return mSkinAppCompatViewInflater.createView(parent,name,context,attrs,inHeritContext,
                isPre2,
                true,
                true);
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}