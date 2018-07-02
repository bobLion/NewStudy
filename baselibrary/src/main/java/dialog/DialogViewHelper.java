package dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @package dialog
 * @fileName DialogViewHelper
 * @Author Bob on 2018/4/18 23:01.
 * @Describe dialog view的辅助处理类
 */

class DialogViewHelper {


    private View  mContentView = null;

    private SparseArray <WeakReference<View>> mViews;
    public DialogViewHelper(Context context, int mViewLayoutResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(mViewLayoutResId,null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    /**
     * 设置布局
     * */
    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    /**
     * 设置文本
     * */
    public void setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
//        TextView tv = mContentView.findViewById(viewId);
        if(null != tv){
            tv.setText(text);
        }

    }

    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewReference = mViews.get(viewId);
        View view = null;
        if(null != viewReference){
            view = viewReference.get();
        }
        if(null == view){
            view = mContentView.findViewById(viewId);
            if(null != view){
                mViews.put(viewId,new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    /**
     * 设置点击事件
     * */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if(null != view){
            view.setOnClickListener(listener);
        }
    }

    /**
     * 获取ContentView
     * */
    public View getContentView() {
        return mContentView;
    }
}
