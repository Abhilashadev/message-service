package com.ascent.message.messageservice.repository;

import com.ascent.message.messageservice.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author abhilasha
 * @since 28/11/19
 */
public interface QueueRepository extends JpaRepository<Queue,String> {

    @Override
    Queue save(Queue messageQueue);

    Queue findByName(String name);

}
