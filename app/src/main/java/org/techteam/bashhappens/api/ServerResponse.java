package org.techteam.bashhappens.api;

public abstract class ServerResponse {
    private String exception = null;

    public String getException() {
        return exception;
    }

    protected void setException(String exception) {
        this.exception = "Error: " + exception;
    }
}
