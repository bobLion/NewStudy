package fixbug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;


/**
 * @package fixbug
 * @fileName FixDexManager
 * @Author Bob on 2018/4/16 22:36.
 * @Describe TODO
 */

public class FixDexManager {

    private Context mContext;
    private File mDexDir;
    private  String TAG = "FixDexManager";


    public FixDexManager(Context context) {
        this.mContext = context;
        //获取目录能够访问的dex目录
        this.mDexDir = mContext.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复dex包
     * @param fixDexPath
     * */
    public void fixDex(String fixDexPath) throws Exception {

        // ② 获取下载好的补丁的dexElement
            //2.1 移动到系统能够访问的dex目录下
        File srcFile = new File(fixDexPath);
        if(!srcFile.exists()){
            throw new FileNotFoundException(fixDexPath);
        }
        File destFile = new File(mDexDir,srcFile.getName());
        if(destFile.exists()){
            Log.d(TAG,"patch["+ fixDexPath +"] has been loaded");
            return;
        }
        copyFile(srcFile,destFile);
            // 2.2 ClassLoader读取fixDex的路径
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        fixDexFiles(fixDexFiles);
    }

    /**
    * 从classLoader中获取dexElement
    * @param classLoader
     * @return
    * */
    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {
        //通过反射先获取PathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        //获取pathList里面的decElement
        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        dexElementField.setAccessible(true);
        return dexElementField.get(pathList);
    }

    /**
     * 文件拷贝
     * */
    public static void copyFile(File src,File dest) throws IOException{
        FileChannel inChannel  = null;
        FileChannel outChannel  = null;

        try {
            if(!dest.exists()){
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0,inChannel.size(),outChannel);

        }finally {
            if(null != inChannel){
                inChannel.close();
            }
            if(null != outChannel){
                outChannel.close();
            }
        }
    }

    /**
     * 合并数组
     * */
    private static Object combineArray(Object arrayLhs,Object arrayRhs){
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass,j);
        for(int k = 0;k<j;++k){
            if(k<i){
                Array.set(result,k,Array.get(arrayLhs,k));
            }else{
                Array.set(result,k,Array.get(arrayRhs,k-i));
            }
        }
        return result;
    }

    /**
     * 加载所有修复 的Dex包
     * */
    public void loadDexFix() throws Exception{
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFile = new ArrayList<>();

        for (File dexFile : dexFiles) {
            if(dexFile.getName().endsWith(".dex")){
                fixDexFile.add(dexFile);
            }
        }
        fixDexFiles(fixDexFile);
    }

    /**
     * 修复dex
     * @param dexFiles
     * */
    private void fixDexFiles(List<File> dexFiles) throws Exception{
        //①  先获取已经运行的DexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object applicationDexElement = getDexElementByClassLoader(applicationClassLoader);

        File optimizedDirectory = new File(mDexDir,"odex");
        if(!optimizedDirectory.exists()){
            optimizedDirectory.mkdirs();
        }
        //修复
        for (File fixDexFile : dexFiles) {
            // dexPath dex路径
            // optimizedDirectory 解压路径
            // libraryPath .so文件位置
            //parent 父classLoader
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),//dex路径
                    optimizedDirectory,//解压路径
                    null,//.so文件位置
                    applicationClassLoader//父classLoader
            );
            Object fixDexElement = getDexElementByClassLoader(fixDexClassLoader);

            // ③ 把补丁的dexElement插到已经运行的decElement的最前面 合并
            applicationDexElement = combineArray(fixDexClassLoader,applicationDexElement);
        }

        //把合并的数组注入到原来的类中 applicationClassLoader
        injectDexElements(applicationClassLoader,applicationDexElement);

    }

    /**
     * 把DexElement注入到classLoader中
     * */
    private void injectDexElements(ClassLoader classLoader, Object dexElement) throws Exception{
        //通过反射先获取PathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        //获取pathList里面的decElement
        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        dexElementField.setAccessible(true);

        dexElementField.set(pathList,dexElement);
    }
}
