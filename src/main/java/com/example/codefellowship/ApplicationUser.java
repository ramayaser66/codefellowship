package com.example.codefellowship;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
//    private boolean isAllowedToEdit = false;
//private boolean isAdmin;


    @Column(unique = true)
   private String username;
   private String password;

   private String lastName;
   private String dateOfBirth;
   private String bio;

   @OneToMany(mappedBy = "applicationUser")
    List<Post> post;


   @ManyToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "followers",

           joinColumns = {
                   @JoinColumn(name = "user_id")},
           inverseJoinColumns = {
                   @JoinColumn(name = "following")})



   private Set<ApplicationUser> followers = new HashSet<>();

    public Set<ApplicationUser> getFollowers() {
        return followers;
    }

    public void Follow(ApplicationUser user){
        this.followers.add(user);
    }

    public void unfollow(ApplicationUser user){
        this.followers.remove(user);
    }

    public boolean isFollowedUser(ApplicationUser user){
        return this.followers.contains(user);
    }





    public ApplicationUser() {

    }

    public ApplicationUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return id;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

//    public boolean isAllowedToEdit() {
//        return isAllowedToEdit;
//    }
//
//    public void setAllowedToEdit(boolean allowedToEdit) {
//        isAllowedToEdit = allowedToEdit;
//    }


    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return
                "username='" + username + '\'' +
                '}';
    }
}

