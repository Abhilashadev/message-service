package com.ascent.message.messageservice.exception;

/**
 * @author abhilasha
 * @since 28/11/19
 */

public class QueueException extends RuntimeException {

    private Integer errorCode;

    public QueueException(String message, Integer errorCode){
        super(message);
        this.errorCode = errorCode;

    }
}
