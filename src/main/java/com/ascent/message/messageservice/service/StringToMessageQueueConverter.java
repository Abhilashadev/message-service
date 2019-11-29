package com.ascent.message.messageservice.service;

import com.ascent.message.messageservice.model.Queue;
import com.ascent.message.messageservice.repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author abhilasha
 * @since 29/11/19
 */

@Component
public class StringToMessageQueueConverter implements Converter<String, Queue> {

    @Autowired
    private QueueRepository queueRepository;


    @Override
    public Queue convert(final String source) {
        return queueRepository.findById(source).get();
    }


}
