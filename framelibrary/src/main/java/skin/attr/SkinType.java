package skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import skin.SkinManager;
import skin.SkinResource;

/**
 * @package skin.attr
 * @fileName SkinType
 * @Author Bob on 2018/5/30 22:47.
 * @Describe TODO
 */

public enum  SkinType {
    TEXT_COLOR ("textColor") {
        @Override
        public void skin(View view, String resourceName) {
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorByName(resourceName);
            if(null == color){
                return;
            }
            TextView textView = (TextView)view;
            textView.setTextColor(color);
        }
    },
    BACKGROUND ("background") {
        @Override
        public void skin(View view, String resourceName) {
            // 背景可能是图片也可能是颜色
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resourceName);
            if(null != drawable){
                ImageView imageView = (ImageView)view;
                imageView.setBackgroundDrawable(drawable);
                return;
            }
            // 如果是颜色
            ColorStateList color = skinResource.getColorByName(resourceName);
            if(null != color){
                view.setBackgroundColor(color.getDefaultColor());
            }
        }
    },
    SRC ("src") {
        @Override
        public void skin(View view, String resourceName) {
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resourceName);
            if(null != drawable){
                ImageView imageView = (ImageView)view;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };

    public SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }

    private String mResName;


    SkinType(String resName){
        this.mResName = resName;
    }
    public abstract  void skin(View view, String resourceName);

    public String getResName() {
        return mResName;
    }
}
