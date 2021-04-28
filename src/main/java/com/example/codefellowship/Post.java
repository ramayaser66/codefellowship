package com.example.codefellowship;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String body;
    private Timestamp createdAt;

    @ManyToOne
    UserApp userApp;

    public Post(){

    }

    public Post(String body, UserApp userApp) {
        this.body = body;
        this.createdAt = new Timestamp(new Date().getTime());
             this.userApp = userApp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Integer getId() {
        return id;
    }

//    public List<Post> getPost() {
//        return this.getPost();
//    }


}
