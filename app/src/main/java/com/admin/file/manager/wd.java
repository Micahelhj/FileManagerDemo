package com.admin.file.manager;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/12 0012 14:35
 * 修改人：Michael-hj
 * 修改时间：2016/5/12 0012 14:35
 * 修改备注：
 */
public class wd {
    private static Context context;
    public static wd application;

    public wd(Context c) {
        context = c;
    }

    public static wd getInstance(Context c) {
        if (application == null)
            application = new wd(c);
        context = c;
        return application;
    }


    /**
     * 保存数据文件.
     *
     * @param data     多媒体数据
     * @param filePath 保存文件路径
     * @param fileName 保存文件名
     * @return 保存成功与否
     */
    public static boolean save2File(InputStream data, String filePath, String fileName) {
        FileOutputStream fos = null;
        try {
            // 写入数据
            fos = new FileOutputStream(getFile(filePath, fileName));
            byte[] b = new byte[1024];
            int len;
            while ((len = data.read(b)) > -1) {
                fos.write(b, 0, len);
            }
            fos.close();

            return true;
        } catch (IOException ex) {

            return false;
        }
    }

    /**
     * 保存为文件.
     *
     * @param data         多媒体数据
     * @param filePathName 保存路径及文件名
     * @return 保存成功或失败
     */
    public static boolean save2File(InputStream data, String filePathName) {
        File file = getFile(filePathName);
        FileOutputStream fos = null;
        try {
            // 写入数据
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = data.read(b)) > -1) {
                fos.write(b, 0, len);
            }
            fos.close();
            return true;
        } catch (IOException ex) {

            return false;
        }
    }

    /**
     * 根据路径+文件名得到一个file
     *
     * @param filePathName
     * @return
     */
    public static File getFile(String filePathName) {
        File file = new File(filePathName);
        file.setLastModified(System.currentTimeMillis());
        FileOutputStream fos = null;
        // 文件或目录不存在时,创建目录和文件.
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 根据路径和文件名得到一个file
     *
     * @param filePath file路径
     * @param fileName file名
     * @return
     */
    public static File getFile(String filePath, String fileName) {
        //path表示你所创建文件的路径
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        // fileName表示你创建的文件名；为txt类型；
        File file = new File(f, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
