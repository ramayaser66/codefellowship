package com.example.codefellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ApplicationUserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup.html";
    }


    @PostMapping("/signup")
    public RedirectView signup(@RequestParam(value="username") String username, @RequestParam(value="password") String password,@RequestParam(value="firstName") String firstName,@RequestParam(value="lastName") String lastName,@RequestParam(value="dateOfBirth") String dateOfBirth , @RequestParam(value="bio") String bio){

        UserApp newUser = new UserApp(username,bCryptPasswordEncoder.encode(password),firstName,lastName,dateOfBirth,bio,"role-user");
        newUser = userRepository.save(newUser);


//        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login.html";
    }


    @GetMapping("/myprofile")
    public String getUserProfilePag1e(Principal p, Model m){
        if(p != null){
            UserApp userApp = userRepository.findByUsername(p.getName());
            List<Post> postcheck = postRepository.findPost(userApp.getId());
            m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
            if(!postcheck.isEmpty()){
                System.out.println("in post section");
                m.addAttribute("posts", postcheck);
                boolean isPostValid = true;
                m.addAttribute("isPostValid",isPostValid);
            }else{
                System.out.println("in message section");

                boolean isPostValid = false;
                m.addAttribute("isPostValid",isPostValid);
                m.addAttribute("message","create your first post");
            }

            return "profile.html";

        }
        return "error.html";

    }



    @GetMapping("/profiles/{id}")
    public String specificProfile(Principal p, Model m, @PathVariable Integer id){

       UserApp requiredProfile = userRepository.findById(id).get();
        System.out.println("we are in getmapping profiels id line 111");
        System.out.println(id);
        System.out.println(requiredProfile);

        if(requiredProfile != null) {

            m.addAttribute("user", requiredProfile);
            String loggedInUserName = p.getName();

//            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
           UserApp loggedInUser = userRepository.findById(id).get();
            System.out.println("checking if allowed");
            System.out.println(loggedInUserName);
            System.out.println(loggedInUser);
            boolean isAllowedToEdit = loggedInUser.getId() == id;
            m.addAttribute("isAllowedToEdit",isAllowedToEdit);

            return "profile.html";
        }else{
            m.addAttribute("message", "this user doesn't exist");
            return "error.html";
        }
    }



    @PutMapping("/profiles/{id}")
    public RedirectView updateProfile(Model m,Principal p, @PathVariable Integer id, @RequestParam String username){

        boolean errorInEditingFlag = false;
        String loggedInUserName = p.getName();
        System.out.println("we are in putmapping profiels id line 133");
        System.out.println(loggedInUserName);

      UserApp loggedInUser = userRepository.findByUsername(loggedInUserName);
//        ApplicationUser loggedInUser = applicationUserRepository.findById(id).get();

        System.out.println(loggedInUser);

        if(loggedInUser.getId() == id) {
            loggedInUser.setUsername(username);
           userRepository.save(loggedInUser);
            return new RedirectView("/login");
        }else{
            errorInEditingFlag = true;

            List<String> errorInEditing = new ArrayList<>();
                errorInEditing.add("sorry you are not authorized to make these changes ...");
                errorInEditing.add("if you wnat to continue please log in ");

            m.addAttribute("error", errorInEditing);
            return new RedirectView(("/error?message=Unauthorized"));

        }

    }






    @GetMapping("/logout")
    public RedirectView getLogout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new RedirectView("/");
    }


    @GetMapping
    public String getHomePage(Principal p, Model m) {
        if(p != null){
            m.addAttribute("user", p.getName());
        }else{
            m.addAttribute("user","please login");
        }


        return "homepage.html";
    }





}
