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
    ApplicationUser applicationUser;

    public Post(){

    }

    public Post(String body, ApplicationUser applicationUser) {
        this.body = body;
        this.createdAt = new Timestamp(new Date().getTime());
        this.applicationUser = applicationUser;
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

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
}
