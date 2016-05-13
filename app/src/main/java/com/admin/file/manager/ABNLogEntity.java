package com.admin.file.manager;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 项目名称：FileManagerDemo
 * 类描述：日志信息实体类
 * 创建人：Michael-hj
 * 创建时间：16/5/13 下午10:45
 * 修改人：Michael-hj
 * 修改时间：16/5/13 下午10:45
 * 修改备注：
 */
public class ABNLogEntity implements Serializable {

    private String dir;

    /**
     * 传值的类型 0 InputStream   1 String
     */
    private int type;
    private String data;
    private InputStream inputStreamDta;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public InputStream getInputStreamDta() {
        return inputStreamDta;
    }

    public void setInputStreamDta(InputStream inputStreamDta) {
        this.inputStreamDta = inputStreamDta;
    }

    public ABNLogEntity() {
    }

    public ABNLogEntity(String dir, InputStream inputStreamDta) {
        this.dir = dir;
        this.type = 0;
        this.inputStreamDta = inputStreamDta;
    }

    public ABNLogEntity(String dir, String data) {
        this.dir = dir;
        this.type = 1;
        this.data = data;
    }
}