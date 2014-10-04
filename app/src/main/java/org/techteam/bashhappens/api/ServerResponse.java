package org.techteam.bashhappens.api;

/**
 * Created by igor on 10/4/14.
 */
public abstract class ServerResponse {
    private String exception = null;

    public String getException() {
        return exception;
    }

    protected void setException(String exception) {
        this.exception = exception;
    }
}
