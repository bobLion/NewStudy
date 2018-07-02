package skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import skin.attr.SkinView;
import skin.config.SkinPreferenceUtils;

/**
 * @package skin
 * @fileName SkinManager
 * @Author Bob on 2018/5/30 22:38.
 * @Describe 皮肤的管理类
 */

public class SkinManager {

    private static SkinManager mInstance;

    private Context mContext;

    private SkinResource mSkinResource;

    private Map<Activity,List<SkinView >> mSkinViews = new HashMap<>();
    static {
        mInstance = new SkinManager();
    }
    public static SkinManager getInstance() {
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();

        // 每次打开应用都会到这里来，做一系列的防止皮肤被任意删除，做一些措施
        String currentSkinPath = SkinPreferenceUtils.getmInstance(mContext).getSkinPath();
        File file = new File(currentSkinPath);
        if(!file.exists()){
            // 文件不存在，清空皮肤
            SkinPreferenceUtils.getmInstance(mContext).clearSkinInfo();
            return;
        }
        // 做一下，能不能获取包名
        String  mPackageName = context.getPackageManager().getPackageArchiveInfo(currentSkinPath,
                PackageManager.GET_ACTIVITIES).applicationInfo.packageName;
        if(TextUtils.isEmpty(mPackageName)){
            SkinPreferenceUtils.getmInstance(mContext).clearSkinInfo();
            return;
        }
        // 最好校验一下签名（增量更新的时候再说）

        // 做一些初始化的工作
        mSkinResource = new SkinResource(mContext,currentSkinPath);

    }

    /**
     * 加载皮肤
     * @param skinPath
     * @return
     */
    public int loadSkin(String skinPath) {
        // 校验签名
        // 初始化资源 管理
      mSkinResource = new SkinResource(mContext,skinPath);
      // 改变皮肤
        Set<Activity> keys =  mSkinViews.keySet();
        for (Activity key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
        }
        // 保存皮肤的状态
        saveSkinStatus(skinPath);
        return 0;
    }

    private void saveSkinStatus(String skinPath) {
        SkinPreferenceUtils.getmInstance(mContext).saveSkinPath(skinPath);
    }

    /**
     * 恢复默认皮肤
     * @return
     */
    public int restoreDefault() {
        return 0;
    }

    /**
     * 获取 SkinView通过activity
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    /**
     * 注册
     * @param activity
     * @param skinViews
     */
    public void register(Activity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity,skinViews);
    }

    /**
     * 获取当前皮肤资源的管理
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }

    /**
     * 检测要不要换肤
     * @param skinView
     */
    public void checkChangeSkin(SkinView skinView) {
        // 如果当前有皮肤，也就是已经保存了皮肤路径，就换一下皮肤
        String currentSkinPath = SkinPreferenceUtils.getmInstance(mContext).getSkinPath();
        if(!TextUtils.isEmpty(currentSkinPath)){
            // 切换皮肤
            skinView.skin();
        }

    }
}
