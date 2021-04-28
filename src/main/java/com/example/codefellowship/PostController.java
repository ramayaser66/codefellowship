package com.example.codefellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PostController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;


    @PostMapping("/post")
    public RedirectView getPost(Integer id, String body){
       UserApp userPost= userRepository.findById(id).get();
        Post posts = new Post(body, userPost);
        postRepository.save(posts);

        return new RedirectView("/myprofile");
    }





}
