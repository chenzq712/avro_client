package com.noway.avro.client.proto;

import java.util.Map;

/**
 * Created by ziqing.chen
 * on 2017/4/18.
 */
public class TestObject {

    private String note;
    private Integer code;
    private Boolean success;
    private Map< String, Object > data;

    public Map< String, Object > getData() {
        return data;
    }

    public void setData( Map< String, Object > data ) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess( Boolean success ) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode( Integer code ) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote( String note ) {
        this.note = note;
    }
}
