package com.trenurbanoapp.model;

/**
 * Created by victor on 12/09/15.
 */
public class IdDesc {

    String id;
    String desc;

    public IdDesc(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
