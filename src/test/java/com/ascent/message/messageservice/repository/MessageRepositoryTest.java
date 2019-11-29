package com.ascent.message.messageservice.repository;

import com.ascent.message.messageservice.fixture.MessageFixture;
import com.ascent.message.messageservice.fixture.QueueFixture;
import com.ascent.message.messageservice.model.Message;
import com.ascent.message.messageservice.model.Queue;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author abhilasha
 * @since 28/11/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@DataJpaTest
public class MessageRepositoryTest {


    @Autowired
    private QueueRepository queueRepository;



    @Test
    public void createsQueueTest(){
        Queue messageQueue = QueueFixture.firstQueue();
        queueRepository.save(messageQueue);
        messageQueue = QueueFixture.secondQueue();
        messageQueue = queueRepository.save(messageQueue);
        List<Queue> messageQueues = queueRepository.findAll();
        Assert.assertNotNull(messageQueues);
        Assert.assertEquals(2, messageQueues.size());
    }

    @Test
    public void addMessageToTheQueueTest(){
        Queue messageQueue = QueueFixture.firstQueue();
        messageQueue = queueRepository.save(messageQueue);

        Message msg = MessageFixture.getMessage();

        messageQueue.getMessages().add(msg);
        messageQueue.size();

        queueRepository.save(messageQueue);
        List<Queue> messageQueues = queueRepository.findAll();
        Assert.assertNotNull(messageQueues);
        Assert.assertEquals(1, messageQueues.size());
        Assert.assertEquals(2, messageQueues.get(0).getMessages().size());
    }
    
    @After
    public void cleanup(){
        queueRepository.deleteAll();
    }
}
