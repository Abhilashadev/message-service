package com.ascent.message.messageservice.service;

import com.ascent.message.messageservice.exception.QueueErrorCode;
import com.ascent.message.messageservice.exception.QueueErrorConstant;
import com.ascent.message.messageservice.exception.QueueException;
import com.ascent.message.messageservice.model.Message;
import com.ascent.message.messageservice.model.Queue;
import com.ascent.message.messageservice.repository.QueueRepository;
import com.ascent.message.messageservice.rest.QueueController;
import com.ascent.message.messageservice.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author abhilasha
 * @since 28/11/19
 */
@Service
@Slf4j
public class QueueServiceImpl implements IQueueService {


    @Autowired
    private QueueRepository queueRepository;

    @Override
    public List<Queue> getQueues() {
        return  queueRepository.findAll();
    }

    @Override
    public Queue createQueue(QueueController.QueueRequestData queueRequestData) {

        Queue queue = queueRepository.findByName(queueRequestData.getName());
        if (queue == null) {
            queue = new Queue(queueRequestData.getName(),queueRequestData.getSize());
            return queueRepository.save(queue);
        }else{
            log.info("can not create message queue as the queue with same name already exists");
            throw new QueueException(QueueErrorConstant.MESSAGE_QUEUE_ALREADY_EXISTS, QueueErrorCode.MESSAGE_QUEUE_ALREADY_EXISTS);
        }

    }

    @Override
    public Queue updateQueue(QueueController.QueueRequestData queueRequestData,String queueId) {
        Queue queue = queueRepository.findById(queueId).orElse(null);
        if (queue == null) {
            throw new QueueException(QueueErrorConstant.MESSAGE_QUEUE_NOT_EXIST, QueueErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        }
        queue.setName(queueRequestData.getName());
        queue.setCapacity(queueRequestData.getSize());
        return queueRepository.save(queue);
    }

    @Override
    public boolean deleteQueue(Queue queue) {
        String queueName = queue.getName();
        queueRepository.delete(queue);
        queue = queueRepository.findById(queue.getId()).orElse(null);
        if(queue == null) {
            log.info("successfully deleted the message queue" + queueName);
            return true;
        }
        log.info("Issue while deleting the message queue" + queueName);
        return false;
    }

    @Override
    public Queue enQueue(Queue queue, String inMessage) {

        if(queue.isFull())
            throw new QueueException(QueueErrorConstant.MESSAGE_QUEUE_IS_FULL,QueueErrorCode.MESSAGE_QUEUE_IS_FULL);
        Message message = new Message();
        message.setBody(CommonUtil.convertToByteArray(inMessage));
        queue.getMessages().add(message);
        queue.size();
        return queueRepository.save(queue);
    }

    @Override
    public Queue deQueue(Queue queue) {
        if(queue.isEmpty())
            throw new QueueException(QueueErrorConstant.MESSAGE_QUEUE_IS_EMPTY,QueueErrorCode.MESSAGE_QUEUE_IS_EMPTY);
        Message message = queue.getMessages().stream().findFirst().orElse(null);
        if(message != null) {
            queue.getMessages().remove(message);
            queue.size();
            queue = queueRepository.save(queue);
            log.info("Successfully deque the first message with id" +message.getId()+"from the the message queue" + queue.getName());
        }
        return queue;
    }

    @Override
    public boolean purge(Queue queue) {

        queue.getMessages().clear();
        queue.size();
        queue = queueRepository.save(queue);
        if(queue.getMessages().isEmpty()) {
            log.info("Successfully purged all the messages from message queue with name" + queue.getName());
            return true;
        }
        log.info("Issue while purging all the messages from message queue with name" + queue.getName());
        return false;
    }

    @Override
    public Message peek(Queue queue) {
        if(queue.isEmpty())
            throw new QueueException(QueueErrorConstant.MESSAGE_QUEUE_IS_EMPTY,QueueErrorCode.MESSAGE_QUEUE_IS_EMPTY);
        return queue.getMessages().stream().findFirst().orElse(null);
    }


}
