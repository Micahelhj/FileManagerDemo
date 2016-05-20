package com.admin.file.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.ClipboardManager;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 项目名称：AdminLibs 类描述： ABIOUtil 创建时间：2016/2/4 14:45 修改人：Michael 修改时间：2016/2/4 14:45 修改备注：
 */
public class ABIOUtil {
    public static final String TAG = ABIOUtil.class.getSimpleName();

    /**
     * 复制功能
     *
     * @param context 上下文
     * @param content 内容
     */
    public static void copy(Context context, String content) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(content);
    }

    /**
     * 关闭流
     *
     * @param closeables the closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                ABLogUtil.e("" + "close IO ERROR...");
            }
        }
    }


    /**
     * 复制文件
     *
     * @param from 当前文件
     * @param to   目标文件
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            ABLogUtil.e("file(from) is null or is not exists!!");
            return;
        }
        if (null == to) {
            ABLogUtil.e("" + "file(to) is null!!");
            return;
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = is.read(buffer))) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            ABLogUtil.e("" + e);
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 从文件中读取文本
     *
     * @param filePath 文件地址
     * @return 文件内容（String）
     */
    @SuppressWarnings("unused")
    public static String readFile(String filePath) {
        StringBuilder resultSb = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            ABLogUtil.e("" + e);
        }
        return inputStream2String(is);
    }

    public static String readFile(File file) {
        return readFile(file.getPath());
    }

    /**
     * 从assets中读取文本
     *
     * @param context 上下文
     * @param name    文件名
     * @return 文件内容（String）
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            ABLogUtil.e("" + e);
        }
        return inputStream2String(is);
    }

    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
            ABLogUtil.e("" + ex);
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    /**
     * 写文本到文件
     *
     * @param path    文件地址
     * @param content 内容
     * @return int
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static int writeFile(String path, String content) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(content.getBytes(Charset.forName("UTF-8")));
            os.flush();
            return 0;
        } catch (Exception e) {
            ABLogUtil.e("" + e);
        } finally {
            ABIOUtil.closeIO(os);
        }
        return -1;
    }

    public static int writeFile(File file, String content) {
        return writeFile(file.getPath(), content);
    }

    /**
     * 将raw文件夹下面的数据库文件写入到手机中
     *
     * @param context 上下文
     * @param rawID   the rawId
     * @param rawName the rawName
     */
    @SuppressLint("SdCardPath")
    public static void writeDBFile(Context context, int rawID, String rawName) {
        //项目数据库固定地址
        final String DATA_BASE_PATH = "/data/data/" + context.getPackageName() + "/databases";
        try {
            File dir = new File(DATA_BASE_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String dbPath = DATA_BASE_PATH + "/" + rawName;
            if (!(new File(dbPath)).exists()) {
                InputStream is = context.getResources().openRawResource(rawID);
                FileOutputStream fos = new FileOutputStream(dbPath);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
        }
    }


    /**
     * Close closable object and wrap {@link IOException} with {@link RuntimeException}
     *
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
        }
    }
}
