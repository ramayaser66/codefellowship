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
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup.html";
    }


    @PostMapping("/signup")
    public RedirectView signup(@RequestParam(value="username") String username, @RequestParam(value="firstName") String firstName,@RequestParam(value="lastName") String lastName, @RequestParam(value="password") String password,@RequestParam(value="dateOfBirth") String dateOfBirth , @RequestParam(value="bio") String bio){

        ApplicationUser newUser = new ApplicationUser(username,bCryptPasswordEncoder.encode(password),firstName,lastName,dateOfBirth,bio);
        newUser = applicationUserRepository.save(newUser);

//        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login.html";
    }

//    @GetMapping("/myprofile")
//    public String getUserProfilePage(Principal p, Model m){
//        ApplicationUser applicationUser = applicationUserRepository.findByUsername(p.getName());
////        applicationUser.setAllowedToEdit(true);
//        m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
//        m.addAttribute("isAllowedToEdit",applicationUser.isAllowedToEdit());
//
//        return "profile.html";
//    }


    //////////////////////////////////////////////
    @GetMapping("/myprofile")
    public String getUserProfilePag1e(Principal p, Model m){
        if(p != null){
            ApplicationUser applicationUser = applicationUserRepository.findByUsername(p.getName());
            List<Post> postcheck = postRepository.findPost(applicationUser.getId());
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

    ////////////////////////////////////////////


//    @GetMapping("/myprofile")
//    public String getUserProfilePage(Principal p, Model m){
//        if(p != null){
//            ApplicationUser applicationUser = applicationUserRepository.findByUsername(p.getName());
//            List<Post> postcheck = postRepository.findPost(applicationUser.getId());
//            m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
//            if(!postcheck.isEmpty()){
//                m.addAttribute("posts", postcheck);
//                boolean isPostValid = true;
//            }else{
//                m.addAttribute("message","create your first post");
//                boolean isPostValid = false;
//            }
//
//            return "profile.html";
//
//        }
//        return "error.html";
//
//    }




//    @GetMapping("/profiles/{id}")
//    public String specificProfile(Principal p, Model m, @PathVariable Integer id){
//        ApplicationUser  requiredProfile = applicationUserRepository.findById(id).get();
//        if(requiredProfile != null) {
//            m.addAttribute("user", requiredProfile);
//            String loggedInUserName = p.getName();
//            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
//            boolean isAllowedToEdit = loggedInUser.getUsername() == p.getName();
//            m.addAttribute("isAllowedToEdit",isAllowedToEdit);
//            return "profile.html";
//        }else{
//            m.addAttribute("message", "this user doesn't exist");
//            return "error.html";
//        }
//    }


    @GetMapping("/profiles/{id}")
    public String specificProfile(Principal p, Model m, @PathVariable Integer id){
        ApplicationUser  requiredProfile = applicationUserRepository.findById(id).get();
        System.out.println("we are in getmapping profiels id line 111");
        System.out.println(id);
        System.out.println(requiredProfile);

        if(requiredProfile != null) {

            m.addAttribute("user", requiredProfile);
            String loggedInUserName = p.getName();

//            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
            ApplicationUser loggedInUser = applicationUserRepository.findById(id).get();
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
    public RedirectView updateProfile(Principal p, @PathVariable Integer id, @RequestParam String username){
        String loggedInUserName = p.getName();
        System.out.println("we are in putmapping profiels id line 133");
        System.out.println(loggedInUserName);

//        ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
        ApplicationUser loggedInUser = applicationUserRepository.findById(id).get();

        System.out.println(loggedInUser);


        if(loggedInUser.getId() == id) {

            loggedInUser.setUsername(username);

            applicationUserRepository.save(loggedInUser);

            return new RedirectView("/profiles/"+id);
        }else{
            return new RedirectView(("/error?message=Unauthorized"));
        }

    }




//    @PutMapping("/profiles/{id}")
//    public RedirectView updateProfile(Principal p, @RequestParam Integer id, @RequestParam String username){
//        String loggedInUserName = p.getName();
//
//        ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
//        ApplicationUser test = applicationUserRepository.findById(id).get();
//        if(test.getId() == id) {
//            loggedInUser.setUsername(username);
//            applicationUserRepository.save(loggedInUser);
//
//            return new RedirectView("/profiles/"+id);
//        }else{
//            return new RedirectView(("/error?message=Unauthorized"));
//        }
//    }


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
