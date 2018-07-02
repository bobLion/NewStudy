package skin.config;

import android.content.Context;

import static skin.config.SkinConfig.SKIN_INFO_NAME;
import static skin.config.SkinConfig.SKIN_PATH_NAME;

/**
 * @package skin.config
 * @fileName SkinPreferenceUtils
 * @Author Bob on 2018/6/6 6:57.
 * @Describe TODO
 */

public class SkinPreferenceUtils {

    private static SkinPreferenceUtils mInstance;
    private Context mContext;

    private SkinPreferenceUtils(Context context){
        this.mContext = context.getApplicationContext();
    }

    public static SkinPreferenceUtils getmInstance(Context context){
        if(null == mInstance){
            synchronized (SkinPreferenceUtils.class){
                if(null == mInstance){
                    mInstance = new SkinPreferenceUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存当前皮肤的路径
     * @param skinPath
     */
    public void saveSkinPath(String skinPath){
        mContext.getSharedPreferences(SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .edit().putString(SKIN_PATH_NAME,skinPath).commit();
    }

    /**
     * 获取皮肤的路径
     * @return 当前皮肤路径
     */
    public String getSkinPath(){
        return mContext.getSharedPreferences(SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .getString(SKIN_PATH_NAME,"");
    }


    /**
     * 清空皮肤路径
     */
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
