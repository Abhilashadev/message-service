package com.ascent.message.messageservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author abhilasha
 * @since 28/11/19
 */
@Getter
@Entity
public class Message  {
    @Id
    @Column(length = 64)
    private String id = UUID.randomUUID().toString();

    @Setter
    private byte[] body;

}
