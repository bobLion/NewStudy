package ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by Bob on 2018/4/11.
 * View的findViewById的辅助类
 */

public class ViewFinder {

    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int viewId){
        return mActivity != null ? mActivity.findViewById(viewId):mView.findViewById(viewId);
    }
}
