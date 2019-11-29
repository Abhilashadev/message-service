package com.ascent.message.messageservice.rest;

import com.ascent.message.messageservice.exception.QueueErrorCode;
import com.ascent.message.messageservice.exception.QueueErrorConstant;
import com.ascent.message.messageservice.exception.QueueException;
import com.ascent.message.messageservice.model.Message;
import com.ascent.message.messageservice.model.Queue;
import com.ascent.message.messageservice.service.IQueueService;
import com.ascent.message.messageservice.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author abhilasha
 * @since 28/11/19
 */


@RepositoryRestController
@RequestMapping(value = "/queues")
public class QueueController {

    @Autowired
    private IQueueService queueService;

    @GetMapping
    public ResponseEntity<Queue> getQueues(){
        List<Queue> queues = queueService.getQueues();
        return new ResponseEntity(queues, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createQueue(@RequestBody QueueRequestData queuedata){
         Queue queue = queueService.createQueue(queuedata);
         return ResponseEntity.ok(queue);

    }

    @PutMapping("/{queueId}")
    public ResponseEntity<Object> updateQueue(@PathVariable(value = "queueId")String queueId,@RequestBody QueueRequestData queuedata) {
        Queue queue = queueService.updateQueue(queuedata,queueId);
        return ResponseEntity.ok(queue);
    }


    @GetMapping("/{queueId}")
    public ResponseEntity<Object> getQueue(@PathVariable(value = "queueId")Queue queue) {
        if(queue == null)
            throw  new QueueException(QueueErrorConstant.MESSAGE_QUEUE_NOT_EXIST, QueueErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        return ResponseEntity.ok(queue);
    }


    @DeleteMapping("/{queueId}")
    public ResponseEntity<Object> deleteQueue(@PathVariable(value = "queueId")Queue queue) {
        boolean isDeleted = queueService.deleteQueue(queue);
        if (isDeleted)
            return ResponseEntity.ok(QueueResourceConstant.QUEUE_DELETED);
        else
            throw new QueueException(QueueErrorConstant.MESSAGE_NOT_PURGED, QueueErrorCode.MESSAGE_NOT_DELETED);
    }

    @PutMapping(value = "/enqueue/{queueId}")
    public ResponseEntity<Object> enqueueMessage(@PathVariable(value = "queueId") Queue messageQueue, @RequestBody MessageContent message){
        if(CommonUtil.isNullOrEmpty(messageQueue))
            throw new QueueException(QueueErrorConstant.MESSAGE_QUEUE_NOT_EXIST, QueueErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        if(CommonUtil.isNullOrEmpty(message))
            throw  new QueueException(QueueErrorConstant.MESSAGE_DETAILS_EMPTY, QueueErrorCode.MESSAGE_DETAILS_EMPTY);
        Queue queue = queueService.enQueue(messageQueue, message.getContent());
        return ResponseEntity.ok(queue);
    }


    @PutMapping(value = "/dequeue/{queueId}")
    public ResponseEntity<Object> dequeueMessage(@PathVariable(value = "queueId") Queue messageQueue){
        if(messageQueue == null)
            throw  new QueueException(QueueErrorConstant.MESSAGE_QUEUE_NOT_EXIST, QueueErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        Queue queue = queueService.deQueue(messageQueue);
        return ResponseEntity.ok(queue);
    }


    @PutMapping(value = "/purge/{queueId}")
    public ResponseEntity<Object> purgeMessages(@PathVariable(value = "queueId") Queue messageQueue){
        if(messageQueue == null)
            throw  new QueueException(QueueErrorConstant.MESSAGE_QUEUE_NOT_EXIST, QueueErrorCode.MESSAGE_QUEUE_NOT_EXIST);

        boolean isPurged = queueService.purge(messageQueue);
        if(isPurged)
            return ResponseEntity.ok(QueueResourceConstant.MESSAGE_PURGED);
        else
            throw new QueueException(QueueErrorConstant.MESSAGE_NOT_PURGED, QueueErrorCode.MESSAGE_NOT_PURGED);
    }


    @GetMapping(value = "/peek/{queueId}")
    public ResponseEntity<Object> peekMessages(@PathVariable(value = "queueId") Queue messageQueue){
        if(messageQueue == null)
            throw  new QueueException(QueueErrorConstant.MESSAGE_QUEUE_NOT_EXIST, QueueErrorCode.MESSAGE_QUEUE_NOT_EXIST);
        Message message = queueService.peek(messageQueue);
        return ResponseEntity.ok(message);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QueueRequestData {
        private String name;
        private int size;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageContent {
        private String content;
    }
}
