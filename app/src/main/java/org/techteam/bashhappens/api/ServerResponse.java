package org.techteam.bashhappens.api;

/**
 * Created by igor on 10/4/14.
 */
public abstract class ServerResponse {
    private Throwable exception = null;

    public Throwable getException() {
        return exception;
    }

    protected void setException(Throwable exception) {
        this.exception = exception;
    }
}
