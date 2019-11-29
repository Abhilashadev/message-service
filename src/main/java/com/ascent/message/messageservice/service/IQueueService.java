package com.ascent.message.messageservice.service;

import com.ascent.message.messageservice.model.Message;
import com.ascent.message.messageservice.model.Queue;
import com.ascent.message.messageservice.rest.QueueController;

import java.util.List;

/**
 * @author abhilasha
 * @since 28/11/19
 */
public interface IQueueService {
    /**
     * Fetches all the queues
     * @return List of queues
     */
    public List<Queue> getQueues();

    /**
     * Creates the Queue with the size provided
     * @param queueRequestData
     * @return Queue
     */
    public Queue createQueue(QueueController.QueueRequestData queueRequestData);

    /**
     * Updates the queue
     * @param queueRequestData
     * @param queueId
     * @return updated Queue
     */
    public Queue updateQueue(QueueController.QueueRequestData queueRequestData,String queueId);

    /**
     * Deletes the queue
     * @param queue
     * @return true if queue is deleted
     */
    public boolean deleteQueue(Queue queue);


    /**
     * @Fuction Adds an  item in the rear of the queue and increments the queue size by 1
     * @param queue
     * @return return Queue
     */

    public Queue enQueue(Queue queue, String message);

    /**
     * @Fuction deletes the item in the front of the queue and decrements the queue size by 1
     * @param queue
     * @return return Queue
     */
    public Queue deQueue(Queue queue);

    /**
     * @Function Deletes all the messages inside the queue
     * @param queue
     * @return true if the messages in the queue deleted else returns false
     */
    public boolean purge(Queue queue);

    /**
     * @Functon Gets the first item in the queue
     * @param queue
     * @return Returns the first message from the queue without deleting.
     */
    public Message peek(Queue queue);
}
