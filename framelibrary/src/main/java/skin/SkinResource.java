package skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @package skin
 * @fileName SkinResource
 * @Author Bob on 2018/5/30 22:39.
 * @Describe 皮肤的资源管理
 */

public class SkinResource {

    // 资源通过这个对象获取
    private Resources mSkinResources;

    private String mPackageName;

    public SkinResource(Context context ,String skinPath) {
        try {
            // 读取本地一个.skin文件里面的资源
            Resources superRes = context.getResources();
            //创建AssetManager
            AssetManager asset = AssetManager.class.newInstance();

            //添加本地下载好的资源皮肤
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
//            method.setAccessible(true);
            method.invoke(asset, skinPath);
            mSkinResources = new Resources(asset,superRes.getDisplayMetrics(),
                    superRes.getConfiguration());

            // 获取skingPath的包名
             mPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath,
                    PackageManager.GET_ACTIVITIES).applicationInfo.packageName;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过名字获取drawable
     * @param resName
     * @return
     */
    public Drawable getDrawableByName(String resName){
        try {
            int resId = mSkinResources.getIdentifier(resName,"drawable",mPackageName);
            Drawable drawable = mSkinResources.getDrawable(resId);
            return drawable;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过名字获取颜色
     * @param resName
     * @return
     */
    public ColorStateList getColorByName(String resName){
        try {
            int resId = mSkinResources.getIdentifier(resName,"color",mPackageName);
            ColorStateList color = mSkinResources.getColorStateList(resId);
            return color;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
