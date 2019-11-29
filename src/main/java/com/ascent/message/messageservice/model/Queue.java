package com.ascent.message.messageservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author abhilasha
 * @since 28/11/19
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Queue {

    @Id
    @Column(length = 64)
    private String id = UUID.randomUUID().toString();

    @Column(unique = true)
    @Setter
    private String name;

    @Setter
    private int capacity;

    @Setter
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinTable(name="queue_message", joinColumns = @JoinColumn(name = "queue_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id"))
    private List<Message> messages = new LinkedList<>();

    private int size;       // current size of the queue


    public Queue(String name,int capacity){
        this.name = name;
        this.capacity = capacity;
    }

    public void size(){
        this.size = messages.size();
    }

    @JsonIgnore
    public boolean isFull(){
        return this.capacity <= this.size;
    }

    @JsonIgnore
    public boolean isEmpty(){
        return this.messages.isEmpty();
    }

}
