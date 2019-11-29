package com.ascent.message.messageservice.exception;

/**
 * @author abhilasha
 * @since 28/11/19
 */

public class QueueErrorConstant {
    public static final String MESSAGE_DETAILS_EMPTY = "Message details are either missing or empty";
    public static final String MESSAGE_QUEUE_NOT_EXIST = "Message queue does not exist";
    public static final String MESSAGE_ID_NOT_PROVIDED = "Message id not provided";
    public static final String MESSAGE_NOT_PURGED = "Issue while purging message, please try again!";
    public static final String MESSAGE_QUEUE_ALREADY_EXISTS = "Message queue with same name already exists";
    public static final String MESSAGE_QUEUE_IS_FULL="Message queue is full, Cannot add new message";
    public static final String MESSAGE_QUEUE_IS_EMPTY="Message queue is empty";


}
