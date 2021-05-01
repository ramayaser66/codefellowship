# codefellowship

## sprig boot 
this web application has the following dependencies:
- dependencies
- Thymeleaf
- Spring Boot DevTools
- Spring Data JPA
- PostgreSQL Driver
- Spring Security


## About this web application 
An application that allows the user to create an account(signup), inter information like 
- username 
- fist name 
- last name 
- bio 
- date of birth 
- password 

you can login and then you will be directed to your profile page, where you can edit your username. 
after you edit you will be directed to login page where you can login and see the changes you made on your profile. 
the user is also allowed to add a post where you can see your posts on your profile. 
the user is also allowed to follow other users. 
this web application offers a page called feed where you can see all the posts of people you followed. 
also it provides the option to see all users joined to this web-application 

routs employed in this web application:


- `signup`
where the user can create an account 
- `/login`
where the user can login 
- `/myprofile`
where the user can see his profile 
- `/profiles/{id}`
if the user is logged in he would have the authorized to add posts and edit their profile.
if you are not logged in you can only see other users profiles 

- `/allUsers`
this allows you to see all the users signed up to this web application

- `/feed`
the user can see all the posts of the user's follower

- `/`
this directs the user to the homepage 

