package com.ascent.message.messageservice.fixture;

import com.ascent.message.messageservice.model.Message;

/**
 * @author abhilasha
 * @since 28/11/19
 */

public class MessageFixture {

    public static Message getMessage(){
        Message message = new Message();
        message.setBody("test".getBytes());
        return message;
    }

    public static Message getMessage1(){
        Message message = new Message();
        message.setBody("test1".getBytes());
        return message;
    }

    public static Message getMessage2(){
        Message message = new Message();
        message.setBody("test2".getBytes());
        return message;
    }

}
