package com.example.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    ApplicationUserRepository applicationUserRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
      if(applicationUser == null){
          System.out.println("user not found... ");
          throw new UsernameNotFoundException("user "+ username+ "does not exists...");
      }
        System.out.println("user" + username+ "found");
      return applicationUser;
    }



}
