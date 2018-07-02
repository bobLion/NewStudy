package navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @package navigationbar
 * @fileName ABSNavigationBar
 * @Author Bob on 2018/4/21 7:16.
 * @Describe 头部的Builder基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements  INavigationBar{

    private P mParams;
    private View mNavigationView;

    public AbsNavigationBar(P params){
        this.mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    /***
     * 设置导航栏文字
     * @param viewId
     * @param text
     */
    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if(!TextUtils.isEmpty(text)){
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    /**
     * 为头部导航栏设置点击事件
     * @param viewId
     * @param listener
     */
    protected void setOnClickListener(int viewId,View.OnClickListener listener){
        findViewById(viewId).setOnClickListener(listener);
    }

    public <T extends View> T findViewById(int viewId){
        return (T) mNavigationView.findViewById(viewId);
    }

    /**
     * 绑定和创建View
     */
    private void createAndBindView() {
        // 创建view
        if(null == mParams.mParent){
            // 获取activity的根布局
//            ViewGroup activityRoot = (ViewGroup) ((Activity)mParams.mContext).findViewById(android.R.id.content);
            ViewGroup activityRoot = (ViewGroup)((Activity)mParams.mContext).getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }
        mNavigationView = LayoutInflater.from(mParams.mContext)
                .inflate(bindLayoutId(),mParams.mParent,false);
        // 添加
        mParams.mParent.addView(mNavigationView,0);

        // 绑定参数
        applyView();
    }

    public abstract  static class Builder{

        public Context mContext;

        public Builder(Context context, ViewGroup parent){
        }

        public abstract AbsNavigationBar builder();

        public static class AbsNavigationParams{
            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context,ViewGroup parent){
                this.mContext = context;
                this.mParent = parent;
            }


        }
    }
}
