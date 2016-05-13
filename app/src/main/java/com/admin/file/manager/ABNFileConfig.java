/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.admin.file.manager;

public class ABNFileConfig {
    /**
     * 手机内部存储的数据库文件
     */
    private static String databases = "databases";
    /**
     * 手机内部SharedPreferences文件
     */
    private static String shared_prefs = "shared_prefs";

    /**
     * 默认下载文件存储目录.
     */
    private static String download_root_dir = "downloads";

    /**
     * 默认下载图片文件存储目录.
     */
    private static String download_image_dir = "images";

    /**
     * 默认下载文件存储目录.
     */
    private static String download_file_dir = "files";

    /**
     * APP缓存目录.
     */
    private static String cache_dir = "caches";
    /**
     * APP异常文件存储目录.
     */
    private static String crach_dir = "crachs";

    /**
     * DB目录.
     */
    public static String DB_DIR = "db";

    /**
     * =================================================================================================
     */

    public static String getDatabases() {
        return databases;
    }

    public void setDatabases(String databases) {
        this.databases = databases;
    }

    public static String getShared_prefs() {
        return shared_prefs;
    }

    public void setShared_prefs(String shared_prefs) {
        this.shared_prefs = shared_prefs;
    }

    public static String getDownload_root_dir() {
        return download_root_dir;
    }

    public static void setDownload_root_dir(String download_root_dir) {
        ABNFileConfig.download_root_dir = download_root_dir;
    }

    public static String getDownload_image_dir() {
        return download_image_dir;
    }

    public static void setDownload_image_dir(String download_image_dir) {
        ABNFileConfig.download_image_dir = download_image_dir;
    }

    public static String getDownload_file_dir() {
        return download_file_dir;
    }

    public static void setDownload_file_dir(String download_file_dir) {
        ABNFileConfig.download_file_dir = download_file_dir;
    }

    public static String getCache_dir() {
        return cache_dir;
    }

    public static void setCache_dir(String cache_dir) {
        ABNFileConfig.cache_dir = cache_dir;
    }

    public static String getCrach_dir() {
        return crach_dir;
    }

    public static void setCrach_dir(String crach_dir) {
        ABNFileConfig.crach_dir = crach_dir;
    }

    public static String getDbDir() {
        return DB_DIR;
    }

    public static void setDbDir(String dbDir) {
        DB_DIR = dbDir;
    }
}
