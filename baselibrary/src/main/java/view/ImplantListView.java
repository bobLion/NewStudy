package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Bob on 2018/4/13.
 * 解决在scrollerView里面嵌套listView显示不全的问题
 */

public class ImplantListView extends ListView{


    public ImplantListView(Context context) {
        super(context);
    }

    public ImplantListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImplantListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
