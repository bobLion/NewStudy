package dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * @package dialog
 * @fileName AlertController
 * @Author Bob on 2018/4/18 23:00.
 * @Describe TODO
 */

class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    public AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    /**
     * 设置文本
     * */
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId,text);

    }

    public <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    /**
     * 设置点击事件
     * */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId,listener);
    }
    /**
     * 获取dialog
     * */
    public AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * 获取dialog的window
     * */
    public Window getWindow() {
        return mWindow;
    }

    public static class AlertParams{

        public Context mContext;
        public int mThemResId;
        public boolean mCancelable = true;//点击空白是否能够取消
        public DialogInterface.OnCancelListener mOnCancelListener;//dialog取消监听
        public DialogInterface.OnDismissListener mOnDismissListener;//dialog消失监听
        public DialogInterface.OnKeyListener mOnKeyListener;//dialog key监听
        // 布局View
        public View mView;
        // 布局的layout ID
        public int mViewLayoutResId;

        // 存放文字的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        // 存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        // 宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        //位置
        public int mGravity = Gravity.CENTER;
        // 动画
        public int mAnimation = 0;


        public AlertParams(Context context, int themId) {
            this.mContext = context;
            this.mThemResId = themId;
        }

        /**
         *  绑定和设置参数
         * */
        public void apply(AlertController mAlert) {
            // 设置布局
            DialogViewHelper dialogViewHelper = null;
            if(mViewLayoutResId != 0){
                dialogViewHelper = new DialogViewHelper(mContext,mViewLayoutResId);
            }
            if(null != mView){
                dialogViewHelper = new DialogViewHelper();
                dialogViewHelper.setContentView(mView);
            }
            if(null == dialogViewHelper){
                throw new IllegalArgumentException("请设置布局 setContentView()");
            }

            // 给dialog设置布局
            mAlert.getDialog().setContentView(dialogViewHelper.getContentView());

            // 设置Controller的辅助类
            mAlert.setViewHelper(dialogViewHelper);

            //设置文本
            int textArraySize = mTextArray.size();
            for(int i = 0;i<textArraySize;i++){
                mAlert.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            // 设置点击事件
            int clickArraySize = mClickArray.size();
            for(int i = 0;i < clickArraySize;i++){
                mAlert.setOnClickListener(mClickArray.keyAt(i),mClickArray.valueAt(i));
            }

            // 配置自定义的效果 全屏 、从底部弹出、默认动画
            Window window = mAlert.getWindow();
            // 设置位置
            window.setGravity(mGravity);
            // 设置动画
            if(0 != mAnimation){
                window.setWindowAnimations(mAnimation);
            }
            // 设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }

}
