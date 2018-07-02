package util;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * @package util
 * @fileName FileUtils
 * @Author Bob on 2018/4/27 16:02.
 * @Describe TODO
 */

public class FileUtils {

    /**
     * 文件复制
     * @param srcFile 源文件
     * @param newFilePath 新文件路径
     * @param fileName 文件名
     * @throws Exception
     */
    public static void copyFile(File srcFile, String  newFilePath,String fileName) throws Exception{
        int byteSum = 0;
        int byteRead = 0;

        // 创建新文件的文件夹
        File file = new File(newFilePath);
        if(!file.exists()){
            file.mkdirs();
        }
        // 获取文件后缀
        String fileType = srcFile.getAbsolutePath().substring(
                srcFile.getAbsolutePath().lastIndexOf("."),
                srcFile.getAbsolutePath().length() - 1);

        // 拼接文件绝对路径
        String fileAbsolutePath = newFilePath +"/" + fileName + "." + fileType;
        // 创建新文件
        File newFile = new File(fileAbsolutePath);
        if(!newFile.exists()){
            newFile.createNewFile();
        }

        File oldFile = new File(srcFile.getAbsolutePath());
        if (oldFile.exists()) {// 源文件存在
            try {
                InputStream inStream = new FileInputStream(srcFile.getAbsolutePath()); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newFile.getAbsolutePath());
                byte[] buffer = new byte[1024];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    byteSum += byteRead; // 字节数 文件大小
                    fs.write(buffer, 0, byteRead);
                }
                fs.close();
                inStream.close();
                Log.d(TAG, "拷贝文件成功,文件总大小为：" + byteSum + "字节");
            } catch (IOException e) {
                Log.e(TAG, "拷贝文件出错：" + e.toString());
                e.printStackTrace();
            }
        } else {// 源文件不存在
            Log.e(TAG, "拷贝文件出错：源文件不存在！");
        }
    }


    /**
     * file转base64
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
            Log.e(TAG,base64);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

    /**
     * base64字符串转文件
     * @param base64
     * @return
     */
    public static File base64ToFile(String base64,File fileName) {
        String filePath = fileName.getAbsolutePath().substring(0,fileName.getAbsolutePath().lastIndexOf("/"));
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(fileName);
            int byteSum = 0;
            int byteRead = 0;
            while ((byteRead = in.read(buffer)) != -1) {
                byteSum += byteRead;
                out.write(buffer, 0, byteRead); // 文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out!= null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @param bitmap         待保存图片
     * @param targetFilePath 图片保存目标地址
     * @return 保存成功返回true，否则返回false
     * @Description 保存图片
     */
    public static boolean saveBitmap(Bitmap bitmap, String targetFilePath) {
        makeFile(targetFilePath);
        File file = new File(targetFilePath);
        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                    fos.flush();
                    fos.close();
                }
                return true;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return false;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * @param filePath 文件路径
     * @return 创建成功返回true，否则返回false
     * @Description 创建文件，如果上层文件夹不存在，则会自动创建文件夹
     */
    public static boolean makeFile(String filePath) {
        String folderName = getFolderName(filePath);
        makeDirs(folderName);
        File file = new File(filePath);
        if ((file.exists() && file.isDirectory())) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static String getFolderName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    public static boolean makeDirs(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            String folderName = getFolderName(filePath);
            if (StringUtils.isEmpty(folderName)) {
                return false;
            }
            File folder = new File(folderName);
            return (folder.exists() && folder.isDirectory()) ? true : folder
                    .mkdirs();
        } else {
            return (file.exists() && file.isDirectory()) ? true : file.mkdirs();
        }
    }

    /**
     * 判断是否存在SD卡
     * @return
     */
    private boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
}
