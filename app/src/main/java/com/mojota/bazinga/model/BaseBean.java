package com.mojota.bazinga.model;

import java.io.Serializable;

/**
 * Created by mojota on 15-8-6.
 */
public class BaseBean implements Serializable {

    private String resultcode;
    private String reason;
    private String error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
