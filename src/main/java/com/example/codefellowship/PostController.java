package com.example.codefellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class PostController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PostRepository postRepository;


    @PostMapping("/post")
    public RedirectView getPost(Integer id, String body){
        ApplicationUser user = applicationUserRepository.findById(id).get();
        Post posts = new Post(body, user);
        postRepository.save(posts);

        return new RedirectView("/myprofile");
    }

//    @PostMapping("/post")
//    public RedirectView getPost(Principal p, String body){
//   String username = p.getName();
//
//   ApplicationUser userpost = applicationUserRepository.findByUsername(username);
//   Post addPost = new Post(body, userpost);
//   postRepository.save(addPost);
//
//        return new RedirectView("/myprofile");
//    }





}
