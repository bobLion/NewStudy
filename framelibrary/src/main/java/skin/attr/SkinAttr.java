package skin.attr;

import android.view.View;

/**
 * @package skin.attr
 * @fileName SkinAttr
 * @Author Bob on 2018/5/30 22:46.
 * @Describe TODO
 */

public class SkinAttr {

    private String mResourceName;

    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResourceName = resName;
        this.mSkinType = skinType;
    }


    public void skin(View view) {
        mSkinType.skin(view,mResourceName);
    }
}
