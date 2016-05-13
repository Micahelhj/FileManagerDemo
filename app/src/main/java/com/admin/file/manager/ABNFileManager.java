package com.admin.file.manager;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * 项目名称：AdminLibs
 * 类描述：本应用数据清除管理器 主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 * 创建时间：2016/2/4 14:45
 * 修改人：Michael
 * 修改时间：2016/2/4 14:45
 * 修改备注：
 */
public class ABNFileManager {
    /**
     * 默认APP根目录.
     */
    private static String downloadRootDir = null;
    /**
     * 默认崩溃日志文件目录
     */
    private static String crachLoadRootDir = null;
    /**
     * 默认日常日志文件目录
     */
    private static String normalLoadRootDir = null;

    /**
     * 默认下载图片文件目录.
     */
    private static String imageDownloadDir = null;

    /**
     * 默认下载文件目录.
     */
    private static String fileDownloadDir = null;

    /**
     * 默认缓存目录.
     */
    private static String cacheDownloadDir = null;

    /**
     * 默认下载数据库文件的目录.
     */
    private static String dbDownloadDir = null;

    /**
     * 描述：初始化存储目录.
     *
     * @param context the context
     */
    public static void initFileDir(Context context) {
        //默认下载图片文件目录.
        String imageDownloadPath = ABNFileConfig.getDownload_image_dir() + File.separator;
        //默认下载文件目录.
        String fileDownloadPath = ABNFileConfig.getDownload_file_dir() + File.separator;
        //默认缓存目录.
        String cacheDownloadPath = ABNFileConfig.getCache_dir() + File.separator;
        //默认CRACH LOG目录.
        String crachLogDownloadPath = ABNFileConfig.getLog_dir() + File.separator + ABNFileConfig.getCrach_dir() + File.separator;
        //默认NORMAL LOG目录.
        String normalLogDownloadPath = ABNFileConfig.getLog_dir() + File.separator + ABNFileConfig.getNormal_dir() + File.separator;
        //默认DB目录.
        String dbDownloadPath = ABNFileConfig.getDbDir() + File.separator;
        try {
            boolean isHaveSDcard = ABSDCardUtils.isAvailable();
            if (isHaveSDcard) {
                //获取根目录
                // 获取文件
                //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
                //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
                File rootFile = context.getExternalFilesDir(null);
                File rootCache = context.getExternalCacheDir();

                /****************************************文件缓存根目录地址*******************************************/
                File downloadDir = new File(rootFile.getAbsolutePath());
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }
                downloadRootDir = downloadDir.getPath();
                /*****************************************缓存文件存储地址******************************************/
                File cacheDownloadDirFile = new File(rootCache.getAbsolutePath());
                if (!cacheDownloadDirFile.exists()) {
                    cacheDownloadDirFile.mkdirs();
                }
                cacheDownloadDir = cacheDownloadDirFile.getPath();
                /****************************************崩溃日志文件存储地址*****************************************/
                File crachDownloadDirFile = new File(rootFile.getAbsolutePath() + "/" + crachLogDownloadPath);
                if (!crachDownloadDirFile.exists()) {
                    crachDownloadDirFile.mkdirs();
                }
                crachLoadRootDir = crachDownloadDirFile.getPath();
                /****************************************日常日志文件存储地址*****************************************/
                File normalDownloadDirFile = new File(rootFile.getAbsolutePath() + "/" + normalLogDownloadPath);
                if (!crachDownloadDirFile.exists()) {
                    crachDownloadDirFile.mkdirs();
                }
                normalLoadRootDir = normalDownloadDirFile.getPath();
                /****************************************图片文件存储地址*******************************************/
                File imageDownloadDirFile = new File(rootFile.getAbsolutePath() + "/" + imageDownloadPath);
                if (!imageDownloadDirFile.exists()) {
                    imageDownloadDirFile.mkdirs();
                }
                imageDownloadDir = imageDownloadDirFile.getPath();
                /****************************************文件下载存储地址*******************************************/
                File fileDownloadDirFile = new File(rootFile.getAbsolutePath() + "/" + fileDownloadPath);
                if (!fileDownloadDirFile.exists()) {
                    fileDownloadDirFile.mkdirs();
                }
                fileDownloadDir = fileDownloadDirFile.getPath();
                /****************************************数据库存放地址*******************************************/
                File dbDownloadDirFile = new File(rootFile.getAbsolutePath() + "/" + dbDownloadPath);
                if (!dbDownloadDirFile.exists()) {
                    dbDownloadDirFile.mkdirs();
                }
                dbDownloadDir = dbDownloadDirFile.getPath();
                /***********************************************************************************/
            } else
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the download root dir.
     *
     * @param context the context
     * @return the download root dir
     */
    public static String getDownloadRootDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return downloadRootDir;
    }


    /**
     * Gets the image download dir.
     *
     * @param context the context
     * @return the image download dir
     */
    public static String getImageDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return imageDownloadDir;
    }


    /**
     * Gets the file download dir.
     *
     * @param context the context
     * @return the file download dir
     */
    public static String getFileDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return fileDownloadDir;
    }


    /**
     * Gets the cache download dir.
     *
     * @param context the context
     * @return the cache download dir
     */
    public static String getCacheDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return cacheDownloadDir;
    }

    /**
     * Gets the crach download dir.
     *
     * @param context the context
     * @return the crach download dir
     */
    public static String getCrachLogDownloadDir(Context context) {
        if (crachLoadRootDir == null) {
            initFileDir(context);
        }
        return crachLoadRootDir;
    }

    /**
     * Gets the crach download dir.
     *
     * @param context the context
     * @return the crach download dir
     */
    public static String getNormalLogDownloadDir(Context context) {
        if (normalLoadRootDir == null) {
            initFileDir(context);
        }
        return normalLoadRootDir;
    }


    /**
     * Gets the db download dir.
     *
     * @param context the context
     * @return the db download dir
     */
    public static String getDbDownloadDir(Context context) {
        if (downloadRootDir == null) {
            initFileDir(context);
        }
        return dbDownloadDir;
    }

    /**
     * =========================================手机内部存储  数据清理==========================================================
     */
    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     *
     * @param context 上下文
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     *
     * @param context 上下文
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File(context.getFilesDir().getPath()
                + context.getPackageName() + "/" + ABNFileConfig.getDatabases()));
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public static void cleanDB() {
        deleteFilesByDirectory(new File(ABNFileConfig.getDbDir() + "/" + ABNFileConfig.getDatabases()));
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     *
     * @param context 上下文
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File(context.getFilesDir().getPath()
                + context.getPackageName() + "/" + ABNFileConfig.getShared_prefs()));
    }

    /**
     * 按名字清除本应用数据库(/data/data/com.xxx.xxx/databases)
     *
     * @param context 上下文
     * @param dbName  数据库名称
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     *
     * @param context 上下文
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }
    /**
     * ===================================================================================
     */

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory 文件夹File对象
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath 文件路径
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath 文件路径
     */
    public static void cleanCustomCache(String... filePath) {
        for (String fp : filePath) {
            cleanCustomCache(fp);
        }
    }

    /**
     * 清除本应用所有的数据（手机内部存储）
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void cleanApplicationData(Context context, String... filePath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String fp : filePath) {
            cleanCustomCache(fp);
        }
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context 上下文
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除image_dir路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    public static void cleanExternalImages() {
        deleteFilesByDirectory(new File(ABNFileConfig.getDownload_image_dir()));
    }

    /**
     * 清除file_dir路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    public static void cleanExternalFiles() {
        deleteFilesByDirectory(new File(ABNFileConfig.getDownload_file_dir()));
    }

    /**
     * 清除root_dir路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    public static void cleanExternalFile() {
        deleteFilesByDirectory(new File(ABNFileConfig.getDownload_root_dir()));
    }

    /**
     * ================================手机外部存储文件删除  清理==================================================================
     */
    /**
     * 清除本应用所有外部存储的数据(即当前文件保存位置)
     *
     * @param context
     * @param filePath
     */
    public static void cleanExternalData(Context context, String... filePath) {
        cleanCustomCache(getCacheDownloadDir(context),
                getDbDownloadDir(context),
                getCrachLogDownloadDir(context),
                getNormalLogDownloadDir(context),
                getImageDownloadDir(context),
                getFileDownloadDir(context));
        for (String fp : filePath) {
            cleanCustomCache(fp);
        }
    }

    /**
     * 删除DB文件
     *
     * @param context
     * @param dbFileName
     */
    public void deleteFileInDbs(Context context, String dbFileName) {
        ABNFileUtil.deleteFile(getDbDownloadDir(context) + "/" + dbFileName);
    }

    /**
     * 删除Images文件
     *
     * @param context
     * @param imageFileName
     */
    public void deleteFileInImages(Context context, String imageFileName) {
        ABNFileUtil.deleteFile(getImageDownloadDir(context) + "/" + imageFileName);
    }

    /**
     * 删除Files文件
     *
     * @param context
     * @param fileName
     */
    public void deleteFileInFiles(Context context, String fileName) {
        ABNFileUtil.deleteFile(getFileDownloadDir(context) + "/" + fileName);
    }

    /**
     * 删除Crachs文件
     *
     * @param context
     * @param crachsFileName
     */
    public void deleteFileInCrachs(Context context, String crachsFileName) {
        ABNFileUtil.deleteFile(getCrachLogDownloadDir(context) + "/" + crachsFileName);
    }

    /**
     * 删除Caches文件
     *
     * @param context
     * @param cachesFileName
     */
    public void deleteFileInCaches(Context context, String cachesFileName) {
        ABNFileUtil.deleteFile(getCacheDownloadDir(context) + "/" + cachesFileName);
    }
    /**
     * ===========================================文件保存、创建  数据保存==================================================
     */
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
     * 文件或目录不存在时,创建目录和文件.
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
     * 文件或目录不存在时,创建目录和文件.
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
        file.setLastModified(System.currentTimeMillis());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * ==================================================文件大小  内存大小相关=============================================
     */

    /**
     * 取得文件夹大小
     *
     * @param file
     * @return
     * @throws Exception 文件大小 long
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * @param size 传入的内存大小
     * @return 格式化单位返回格式化之后的值
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 获取手机指定位置可用空间大小
     *
     * @return
     */

    public static long getAvailableMemorySize(String filePath) {
        try {
            if (ABSDCardUtils.isAvailable()) {
                StatFs statFs = new StatFs(filePath);
                long blockSize = statFs.getBlockSize();
                long availableBlocks = statFs.getAvailableBlocks();
                return availableBlocks * blockSize;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return -1;
    }
    /**
     * ==========================================================================================================
     */
}
