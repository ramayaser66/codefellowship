package com.example.codefellowship;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserApp, Integer> {
    public UserApp findByUsername(String username);
}
