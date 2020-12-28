package com.revature.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Request {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "story")
    private Story story;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender")
    private User sender;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver")
    private User receiver;
    private String message;

    public Request() {
        id = 0;
        story = null;
        sender = null;
        receiver = null;
        message = "";
    }

    public Request(Integer id, Story story, User sender, User receiver, String message) {
        this.id = id;
        this.story = story;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(id, request.id) && Objects.equals(story, request.story) && Objects.equals(sender, request.sender) && Objects.equals(receiver, request.receiver) && Objects.equals(message, request.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, story, sender, receiver, message);
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", story=" + story +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + message + '\'' +
                '}';
    }
}
