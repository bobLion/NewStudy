package skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import skin.attr.SkinAttr;
import skin.attr.SkinType;


/**
 * @package skin
 * @fileName SkinAttrSupport
 * @Author Bob on 2018/5/30 22:40.
 * @Describe 皮肤属性解析的支持类
 */

/**
 * 获取SkinAttrs的属性
 */
public class SkinAttrSupport {
    private static final String TAG = "TAG";
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        // background src ,text color
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int attrLength = attrs.getAttributeCount();
        for(int index = 0;index< attrLength;index++){
            // 获取名称  值
            String attrName = attrs.getAttributeName(index);
            String attrValue = attrs.getAttributeValue(index);

            // Log.e(TAG,"attrName：  "+attrName +"    attrValue:   "+attrValue);
            // 只获取重要的
            SkinType skinType = getSkinType(attrName);
            if(null != skinType){
                // 资源名称 目前只有attrValue 且是以@开头的int值
                String resName = getResName(context,attrValue);
                if(TextUtils.isEmpty(resName)){
                    continue;
                }
                SkinAttr skinAttr = new SkinAttr(resName,skinType);
                skinAttrs.add(skinAttr);
            }
        }
        return null;
    }

    /**
     * 获取资源名称
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {
        if(attrValue.startsWith("@")){
            attrValue = attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 通过名称获取SkinType
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if(skinType.getResName().equals(attrName)){
                return skinType;
            }
        }
        return null;
    }
}
