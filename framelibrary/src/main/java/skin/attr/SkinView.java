package skin.attr;

import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * @package skin.attr
 * @fileName SkinView
 * @Author Bob on 2018/5/30 22:45.
 * @Describe TODO
 */

public class SkinView {

    private View mView;

    private List<SkinAttr> mAttrs;


    public SkinView(View view, List<SkinAttr> skinAttrList) {
        this.mView = view;
        this.mAttrs = skinAttrList;
    }

    public void skin(){
        for (SkinAttr attr : mAttrs) {
            attr.skin(mView);
        }
    }
}
