package com.example.codefellowship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ApplicationUser implements UserDetails {


 UserApp userApp;

//
//   @OneToMany(mappedBy = "applicationUser")
//    List<Post> post;


    public ApplicationUser(UserApp userApp) {
        this.userApp = userApp;

    }



//    public ApplicationUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio) {
//        this.username = username;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.dateOfBirth = dateOfBirth;
//        this.bio = bio;
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userApp.getAuthority());
        List<SimpleGrantedAuthority>  userAutjorities = new ArrayList<SimpleGrantedAuthority>();
        userAutjorities.add(simpleGrantedAuthority);
        return userAutjorities;
    }



    @Override
    public String getPassword() {
        return  userApp.getPassword();
    }

    @Override
    public String getUsername() {
        return  userApp.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return userApp.getId();
    }

//
//    public void setUsername(String username) {
//        user.getUsername().equals(username);
//    }


    public String getFirstName() {
        return userApp.getFirstName();
    }



    public String getLastName() {
        return userApp.getLastName();
    }



    public String getDateOfBirth() {
        return userApp.getDateOfBirth();
    }



    public String getBio() {
        return userApp.getBio();
    }



//    public boolean isAllowedToEdit() {
//        return isAllowedToEdit;
//    }
//
//    public void setAllowedToEdit(boolean allowedToEdit) {
//        isAllowedToEdit = allowedToEdit;
//    }


//    public List<Post> getPost() {
//        return user.getPost();
//    }
}

