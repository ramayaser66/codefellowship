package com.example.codefellowship;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query(value = "SELECT * FROM post where user_app_id = ?1", nativeQuery = true)
    List<Post> findPost(int id);

}
