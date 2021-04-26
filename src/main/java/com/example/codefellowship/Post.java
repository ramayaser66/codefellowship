package com.example.codefellowship;

import javax.persistence.*;
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
    



}
