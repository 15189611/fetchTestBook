package com.handy.fetchbook.basic.vlayout.exeption;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.Nullable;

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
public class BaseAdapterDataException extends RuntimeException {
    public Throwable realCause;
    public Object item;

    public BaseAdapterDataException() {
    }

    public BaseAdapterDataException(String message) {
        super(message);
    }

    public BaseAdapterDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseAdapterDataException(Throwable cause) {
        this(cause, null);
    }

    public BaseAdapterDataException(Throwable cause, @Nullable Object item) {
        super(cause);
        this.realCause = cause;
        this.item = item;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public BaseAdapterDataException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}