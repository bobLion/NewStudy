package com.bob.android.framelibrary.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bob.android.framelibrary.R;

import navigationbar.AbsNavigationBar;

/**
 * @package com.bob.android.framelibrary
 * @fileName DefaultNavigationBar
 * @Author Bob on 2018/4/21 7:59.
 * @Describe TODO
 */

public class DefaultNavigationBar extends
        AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {


    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        // 绑定效果
        setText(R.id.tv_title,getParams().mTitle);
        setText(R.id.tv_right_text,getParams().mRightText);
        setOnClickListener(R.id.tv_right_text,getParams().mRightClickListener);
        setOnClickListener(R.id.img_back,getParams().mLeftClickListener);
    }



    public static class Builder extends AbsNavigationBar.Builder{

        DefaultNavigationParams P ;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context,parent);
        }

        public Builder(Context context) {
            super(context, null);
            P = new DefaultNavigationParams(context,null);
        }

        @Override
        public DefaultNavigationBar builder() {
            DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar(P);
            return defaultNavigationBar;
        }

        // 设置所有的效果

        /**
         * 设置标题
         * @param title
         * @return
         */
        public DefaultNavigationBar.Builder setTitle(String title ){
            P.mTitle = title;
            return this;
        }

        /**
         * 设置右边文字
         * @param rightText
         * @return
         */
        public DefaultNavigationBar.Builder setRightText(String rightText ){
            P.mRightText = rightText;
            return this;
        }

        /***
         * 设置左边图片
         * @param leftIconId
         * @return
         */
        public DefaultNavigationBar.Builder setLeftIcon(int leftIconId ){
            P.mLeftIconId = leftIconId;
            return this;
        }


        /***
         * 设置右边图片
         * @param rightIconId
         * @return
         */
        public DefaultNavigationBar.Builder setRightIcon(int rightIconId ){
            P.mRightIconId = rightIconId;
            return this;
        }

        /**
         * 左边的icon点击事件
         * @param leftClickListener
         * @return
         */
        public DefaultNavigationBar.Builder setLeftClickListener(View.OnClickListener leftClickListener){
            P.mLeftClickListener = leftClickListener;
            return this;
        }


        /**
         * 右边的icon点击事件
         * @param rightClickListener
         * @return
         */
        public DefaultNavigationBar.Builder setRightClickListener(View.OnClickListener rightClickListener){
            P.mRightClickListener = rightClickListener;
            return this;
        }


        public static class DefaultNavigationParams extends
                AbsNavigationBar.Builder.AbsNavigationParams{

            public String mTitle;
            public String mRightText;
            public int mRightIconId;
            public int mLeftIconId;
            public View.OnClickListener mRightClickListener;
            public View.OnClickListener mLeftClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 把当前activity关闭
                    ((Activity)mContext).finish();
                }
            };



            // 所有的效果放在这里
            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
