package com.ascent.message.messageservice.service;

import com.ascent.message.messageservice.fixture.QueueFixture;
import com.ascent.message.messageservice.model.Message;
import com.ascent.message.messageservice.model.Queue;
import com.ascent.message.messageservice.repository.QueueRepository;
import com.ascent.message.messageservice.rest.QueueController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author abhilasha
 * @since 28/11/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QueueServiceTest {

    @Autowired
    private QueueRepository messageQueueRepository;

    @Autowired
    private IQueueService iQueueService;

    @Before
    public void cleanup(){
        messageQueueRepository.deleteAll();
    }


    @Test
    public void createQueueTest(){
        QueueController.QueueRequestData queueRequestData = new QueueController.QueueRequestData("test",5);
        Queue messageQueue = iQueueService.createQueue(queueRequestData);
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(0, messageQueue.getSize());
    }


    @Test
    public void updateQueueNameTest(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        QueueController.QueueRequestData queueRequestData = new QueueController.QueueRequestData("test",5);
        messageQueue = iQueueService.updateQueue(queueRequestData, messageQueue.getId());
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(1, messageQueue.getSize());
        Assert.assertEquals("test", messageQueue.getName());
    }

    @Test
    public void DeleteQueueTest(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        boolean isDeleted = iQueueService.deleteQueue(messageQueue);
        Assert.assertTrue(isDeleted);
    }

    @Test
    public void enqueMessageToExistingQueue(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        String message = "test";
        messageQueue = iQueueService.enQueue(messageQueue, message);
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(2, messageQueue.getSize());
    }

    @Test
    public void enqueMessageToExistingQueue1(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iQueueService.enQueue(messageQueue, "test1");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(2, messageQueue.getSize());
    }

    @Test
    public void dequeMessageFromExistingQueue(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iQueueService.enQueue(messageQueue, "test");
        messageQueue = iQueueService.enQueue(messageQueue, "test1");
        messageQueue = iQueueService.enQueue(messageQueue, "test2");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(4, messageQueue.getSize());
        messageQueue = iQueueService.deQueue(messageQueue);
        Assert.assertEquals(3, messageQueue.getSize());

    }


    @Test
    public void purgeMessagesFromExistingQueue(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iQueueService.enQueue(messageQueue, "test");
        messageQueue = iQueueService.enQueue(messageQueue, "test1");
        messageQueue = iQueueService.enQueue(messageQueue, "test2");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(4, messageQueue.getSize());
        boolean isPurged = iQueueService.purge(messageQueue);
        Assert.assertEquals(0, messageQueue.getSize());
        Assert.assertTrue(isPurged);
    }

    @Test
    public void peekMessageFromExistingQueue(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = messageQueueRepository.save(messageQueue);
        messageQueue = iQueueService.enQueue(messageQueue, "test");
        messageQueue = iQueueService.enQueue(messageQueue, "test1");
        messageQueue = iQueueService.enQueue(messageQueue, "test2");
        Assert.assertNotNull(messageQueue);
        Assert.assertEquals(4, messageQueue.getSize());
        Message message = iQueueService.peek(messageQueue);
        Assert.assertEquals(4, messageQueue.getSize());
        Assert.assertNotNull(message);
    }

}
