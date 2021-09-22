package com.zgt.opengauss.zeus.common;


public class FailException extends RuntimeException {
    public FailException(String msg) {
        super(msg);
    }
}
