package org.techteam.bashhappens.api;

import android.util.SparseArray;

public class TranslateErrors {
    public static final int ERR_OK = 200;
    public static final int ERR_KEY_INVALID = 401;
    public static final int ERR_KEY_BLOCKED = 402;
    public static final int ERR_DAILY_REQ_LIMIT_EXCEEDED = 403;
    public static final int ERR_DAILY_CHAR_LIMIT_EXCEEDED = 404;
    public static final int ERR_TEXT_TOO_LONG = 413;
    public static final int ERR_UNPROCESSABLE_TEXT = 422;
    public static final int ERR_LANG_NOT_SUPPORTED = 501;
    private static final SparseArray<String> ERRORS = new SparseArray<String>(8);

    static {
        ERRORS.put(ERR_OK, "Operation completed successfully.");
        ERRORS.put(ERR_KEY_INVALID, "Invalid API key.");
        ERRORS.put(ERR_KEY_BLOCKED, "This API key has been blocked.");
        ERRORS.put(ERR_DAILY_REQ_LIMIT_EXCEEDED, "You have reached the daily limit for requests");
        ERRORS.put(ERR_DAILY_CHAR_LIMIT_EXCEEDED, "You have reached the daily limit for the volume of translated text ");
        ERRORS.put(ERR_TEXT_TOO_LONG, "The text size exceeds the maximum.");
        ERRORS.put(ERR_UNPROCESSABLE_TEXT, "The text could not be translated.");
        ERRORS.put(ERR_LANG_NOT_SUPPORTED, "The specified translation direction is not supported.");
    }

    public static String getErrorMessage(int errorCode) {
        return ERRORS.get(errorCode);
    }
}
