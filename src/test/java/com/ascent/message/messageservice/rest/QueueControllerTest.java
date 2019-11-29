package com.ascent.message.messageservice.rest;

import com.ascent.message.messageservice.fixture.QueueFixture;
import com.ascent.message.messageservice.model.Message;
import com.ascent.message.messageservice.model.Queue;
import com.ascent.message.messageservice.repository.QueueRepository;
import com.ascent.message.messageservice.service.IQueueService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * @author abhilasha
 * @since 28/11/19
 */

@RunWith(SpringRunner.class)
@WebMvcTest(QueueController.class)
public class QueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQueueService iQueueService;

    @MockBean
    private QueueRepository queueRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createQueueTest() throws Exception{
        Queue queue = QueueFixture.firstQueue();

        when(iQueueService.createQueue(anyObject())).thenReturn(queue);

        Map<String,Object> requestMap = new HashMap<>(0);
        requestMap.put("name","TestQueue");
        requestMap.put("size",5);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/queues")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMap))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
    }


    @Test
    public void getQueueTest() throws Exception{
        Queue messageQueue = QueueFixture.firstQueue();
        when(queueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));

        MockHttpServletResponse response= mockMvc.perform( MockMvcRequestBuilders
                .get("/queues/{queueId}",messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        Assert.assertEquals(200, response.getStatus());
        Queue resultQueue = this.objectMapper.readValue(response.getContentAsString(), Queue.class);
        Assert.assertNotNull(resultQueue);
        Assert.assertEquals(messageQueue.getId(), resultQueue.getId());


    }


    @Test
    public void deleteQueueTest() throws Exception{
        Queue messageQueue = QueueFixture.firstQueue();
        when(queueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iQueueService.deleteQueue(any())).thenReturn(true);


        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/queues/{queueId}",messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Assert.assertEquals(200, response.getStatus());
        String resultResponse = response.getContentAsString();
        Assert.assertEquals(QueueResourceConstant.QUEUE_DELETED, resultResponse);

    }

    @Test
    public void enqueMessageToNewQueue()throws Exception{

        Queue messageQueue = QueueFixture.firstQueue();
        when(queueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iQueueService.enQueue(any(), anyString())).thenReturn(messageQueue);


        Map<String,Object> requestMap = new HashMap<>(0);
        requestMap.put("content","test");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/queues/enqueue/{queueId}", messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(requestMap))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        Queue resultQueue = objectMapper.readValue(response.getContentAsString(), Queue.class);
        Assert.assertNotNull(resultQueue);
        Assert.assertNotNull(resultQueue.getId());

        Assert.assertEquals(messageQueue.getName(), resultQueue.getName());
        Assert.assertEquals(1, resultQueue.getSize());
        Assert.assertEquals(1, resultQueue.getMessages().size());
    }


    @Test
    public void dequeueMessageFromExistingQueue()throws Exception{

        Queue messageQueue = QueueFixture.firstQueue();
        when(queueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iQueueService.deQueue(any())).thenReturn(messageQueue);

        this.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/queues/dequeue/{queueId}", messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        Queue resultQueue = objectMapper.readValue(response.getContentAsString(), Queue.class);
        Assert.assertNotNull(resultQueue);
        Assert.assertNotNull(resultQueue.getId());
        Assert.assertEquals(messageQueue.getName(), resultQueue.getName());

    }


    @Test
    public void purgeMessagesFromExistingQueue() throws Exception{

        Queue messageQueue = QueueFixture.firstQueue();
        when(queueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
        when(iQueueService.purge(any())).thenReturn(true);

        this.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/queues/purge/{queueId}", messageQueue.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("Messages purged!", response.getContentAsString());

    }


//    @Test
//    public void peekMessagesFromExistingQueue() throws Exception{
//
//        Queue messageQueue = QueueFixture.firstQueue();
//        when(queueRepository.findById(messageQueue.getId())).thenReturn(Optional.of(messageQueue));
//        when(iQueueService.peek(any())).thenReturn(messageQueue.getMessages().get(0));
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/queues/peek/{queueId}", messageQueue.getId())
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON_UTF8);
//
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//        MockHttpServletResponse response = result.getResponse();
//
//        Message message = this.objectMapper.readValue(response.getContentAsString(), Message.class);
//        Assert.assertEquals(200, response.getStatus());
//        Assert.assertEquals(messageQueue.getMessages().get(0).getId(), message.getId());
//
//    }



}
