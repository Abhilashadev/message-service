package com.ascent.message.messageservice.fixture;

import com.ascent.message.messageservice.model.Message;
import com.ascent.message.messageservice.model.Queue;

/**
 * @author abhilasha
 * @since 28/11/19
 */

public class QueueFixture {


    public static Queue firstQueue(){
        Queue messageQueue = new Queue("test",5);
        Message message = MessageFixture.getMessage();
        messageQueue.getMessages().add(message);
        messageQueue.size();

        return messageQueue;
    }

    public static Queue secondQueue(){
        Queue messageQueue = new Queue("test1",5);
        Message message = MessageFixture.getMessage1();
        messageQueue.getMessages().add(message);
        messageQueue.size();

        return messageQueue;
    }

}
