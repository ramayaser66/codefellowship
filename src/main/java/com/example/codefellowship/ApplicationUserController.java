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
import java.util.Optional;


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







//    @GetMapping("/profiles/{id}")
//    public String specificProfile(Principal p, Model m, @PathVariable Integer id){
//
//        ApplicationUser  requiredProfile = applicationUserRepository.findById(id).get();
//
//
//        if(requiredProfile != null) {
//
//            m.addAttribute("user", requiredProfile);
//            String loggedInUserName = p.getName();
//
//            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
////            ApplicationUser loggedInUser = applicationUserRepository.findById(id).get();
//            System.out.println("checking if allowed");
//            System.out.println(loggedInUserName);
//            System.out.println(loggedInUser);
//            boolean isAllowedToEdit = loggedInUser.getId() == id;
//            m.addAttribute("isAllowedToEdit",isAllowedToEdit);
//
//            return "profile.html";
//        }else{
//            m.addAttribute("message", "this user doesn't exist");
//            return "error.html";
//        }
//    }


    //////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/profiles/{id}")
    public String specificProfile(Principal p, Model m, @PathVariable Integer id) {
        try {

            String username = p.getName();

            ApplicationUser current = applicationUserRepository.findByUsername(username);



            ApplicationUser requiredProfile = applicationUserRepository.findById(id).get();


            boolean isFollowed = current.isFollowedUser(requiredProfile);

            boolean isUser = false;

            m.addAttribute("user", requiredProfile);
            m.addAttribute("isUser", isUser);
            m.addAttribute("username", username);
            m.addAttribute("isFollowed ", isFollowed);

            String loggedInUserName = p.getName();
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
//            ApplicationUser loggedInUser = applicationUserRepository.findById(id).get();

            boolean isAllowedToEdit = loggedInUser.getId().equals(id);
            m.addAttribute("isAllowedToEdit", isAllowedToEdit);


            if (username.equals(requiredProfile.getUsername())) {
                System.out.println("requiredProfilet**********************************");
                System.out.println(requiredProfile);
                System.out.println("**********************************");
                System.out.println(" current**********************************");
                System.out.println(current);
                System.out.println("**********************************");

                isUser = true;
                System.out.println("11111111111111111111111111111111111111");
                System.out.println(isFollowed);
                System.out.println(isUser);
                System.out.println("11111111111111111111111111111111111111");

                m.addAttribute("user", requiredProfile);
                m.addAttribute("isUser", isUser);
                m.addAttribute("username", username);
                m.addAttribute("isFollowed ", isFollowed);

            }
            return "profile.html";
        }catch (Exception ex){
            return "error..."+ ex;
        }
    }

//    else{
//        m.addAttribute("message", "this user doesn't exist");
//        return "error.html";
//    }



    @PostMapping("/profiles/{username}")
    public RedirectView follow (Principal p, @PathVariable String username, Model m){

        ApplicationUser  current = applicationUserRepository.findByUsername(p.getName());
       ApplicationUser followUser = applicationUserRepository.findByUsername(username);
       if(!current.equals(followUser)){
           current.Follow(followUser);
           applicationUserRepository.save(current);
       }else{
           m.addAttribute("message", "this user doesn't exist");
         return new RedirectView("/error?message=Unauthorized");
       }



       return new RedirectView("/profiles/"+followUser.getId());
    }



















    ////////////////////////////////////////

    @PutMapping("/profiles/{id}")
    public RedirectView updateProfile(Principal p, @PathVariable Integer id, @RequestParam String username){
        String loggedInUserName = p.getName();
//        ApplicationUser loggedInUser = applicationUserRepository.findByUsername(loggedInUserName);
        ApplicationUser loggedInUser = applicationUserRepository.findById(id).get();
        System.out.println(loggedInUser);
        if(loggedInUser.getId() == id) {
            loggedInUser.setUsername(username);
            applicationUserRepository.save(loggedInUser);
            return new RedirectView("/login");
        }else{
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



    @GetMapping("/allUsers")
    public String getUsers(Model m, Principal p) {
        if(p != null){
        List<ApplicationUser> AllUsers= (List<ApplicationUser>) applicationUserRepository.findAll();
            m.addAttribute("userH", AllUsers);
       ApplicationUser LogedInUser = applicationUserRepository.findByUsername(p.getName());

            System.out.println(LogedInUser);

            if(AllUsers.contains(LogedInUser)){
                AllUsers.remove(LogedInUser);
            }
            m.addAttribute("user", p.getName());
////
        }else{
            m.addAttribute("userH","please login");
        }
        return "allUsers.html";
    }




    @GetMapping("/feed")
    public String getfeed(Model m, Principal p) {

        return "feed.html";
    }




}
