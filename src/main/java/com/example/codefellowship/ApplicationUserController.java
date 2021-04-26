package com.example.codefellowship;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;


@Controller
public class ApplicationUserController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

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


    @GetMapping("/myprofile")
    public String getUserProfilePage(Principal p, Model m){
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(p.getName());
        m.addAttribute("user", ((UsernamePasswordAuthenticationToken)p).getPrincipal());
        return "profile.html";
    }




    @GetMapping("/profiles/{id}")
    public String specificProfile(Principal p, Model m, @PathVariable Integer id){
        ApplicationUser  requiredProfile = applicationUserRepository.findById(id).get();
        if(requiredProfile != null) {
            m.addAttribute("user", requiredProfile);
            String loggedInUserName = p.getName();
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
            boolean isAllowedToEdit = loggedInUser.getUsername() == p.getName();
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

        ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
        if(loggedInUser.getId() == id) {
            loggedInUser.setUsername(username);
            applicationUserRepository.save(loggedInUser);

            return new RedirectView("/profiles/"+id);
        }else{
            return new RedirectView(("/error?message=Unauthorized"));
        }
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
