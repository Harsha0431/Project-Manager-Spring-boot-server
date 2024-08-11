## Project Statement
> This is a  web application where user can display their projects and portfolios.


This directory contains all the files related to server side i.e.,Spring Boot
## Structure of each module
> Here each module contains of 4 components
> 1. **Models:** Contains table schemas
> 2. **Repository:** Here all kind of CRUD operations are performed in two different ways
>> **2.1** By extending repository class with CrudRepository. Here we have no need to explicitly perform any kind of CRUD operations. <br>(**Author Preference:** Myself I feel this approach doesn't give much flexibility for user but it preferable if we don't have much complex queries.) 
>> <br>**2.2** Here we create and manage all our transactions with help of session factory bean.
> 3. **Service:** These classes are responsible to communicate with repository classes on behalf of controller (REST client)
> 4. **Controller:** Here api routes are mapped with corresponding classes, these classes will perform desired operations with help of service classes.

## Commonly used annotations
1. **@Component**
> **Purpose:** A generic stereotype for any class that can be managed by the Spring container.<br>
> **Use Case:** It's a broader annotation that can be used for any component that doesn't fit into the more specific categories. For example, a utility class or a configuration class.

2. **@Repository**
> **Purpose:** Indicates a class that performs data access operations.<br>
> **Use Case:** Typically used for classes that interact with databases, such as those implementing data access objects (DAOs). It often provides additional features like exception translation and transaction management.

3. **@Service**
> **Purpose:** Indicates a class that provides business logic.<br>
> **Use Case:** Used for classes that encapsulate business rules and coordinate the application's core functions. It often interacts with repositories to retrieve and manipulate data.

4. **@Controller**
> **Purpose:** Indicates a class that handles incoming web requests and returns a model and view to be rendered.<br>
> **Use Case:** Used for classes that define the application's web endpoints and process user requests. It's typically used in conjunction with frameworks like ***Spring MVC***.

5. **@RestController**
> **Purpose: **It is a specialized version of the ***@Controller***. Indicates a class that handles incoming web requests and returns a model<br>
> **Use Cases:** 
> * Building ***RESTful APIs*** for consumption by clients like mobile apps, web applications, or other services.
> * ***@RestController*** is primarily used for returning data (JSON, XML, etc.) rather than rendering views.

6. **@AutoWired**
> * It is a core annotation in Spring used for *dependency injection ***(DI)****.
> * ***@Autowired*** essentially tells Spring to automatically inject a bean of the specified type into a field without you having to manually create an instance of it.

```java
package com.p1;
@Component
public class Bean1 {
}

package com.p2;
@Component
public class Bean2 {
    @Autowired
    Bean1 bean1;
}
```

## Module-1: Authentication

1. **User model**
```java User.java
    package com.model;
    import jakarta.persistence.*;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    
    @Entity
    @Table(name = "user", uniqueConstraints = {
            @UniqueConstraint(name = "uk_user", columnNames = {"username"})
        }
    )
    public class User {
        @Id
        @Column(name = "email")
        private String email;
        @Column(name = "username", nullable = false)
        private  String username;
        @Column(name = "password", nullable = false)
        private String password;
    
        // Getters and Setters goes here
        // As we want to store user password as encrypted, so we use below approch
        public void setPassword(String password) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.password = encoder.encode(password);
        }
    
        // toString() method goes here
    }
```

2. **Repository**
<br>Here we perform CRUD operations on User model
```java UserRepository
package com.repository;

import com.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
```
```java UserCustomRepository
package com.repository;
// Import related packages
@Service
public class UserCustomRepository {
    @Autowired
    private SessionFactory sessionFactory;

    public ApiResponse<User> addUser(User user){
        Session session = sessionFactory.openSession();
        try{
            session.getTransaction().begin();
            session.persist("User", user);
            session.getTransaction().commit();
        }
        catch (Exception e){
            // Do some debugging
        }
        finally {
            session.close();
        }
        // return new ApiResponse<User>(status, message, data);
    }
}
```
3. Service
```java
package com.service;
// Import required packages
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCustomRepository userCustomRepository;

    public ApiResponse<User> saveUser(User user){
        try{
            // Do something
            // return userCustomRepository.addUser(user);  # Using custom repository
            User savedObject = userRepository.save(user);
             return new ApiResponse<>(1, "User created successfully", savedObject);
        } catch(Exception e){
            // Handle exception
        }
    }
    
    // Other functions goes here
}
```
4. **Controller**
```java
package com.controller;

import com.ApiResponse.ApiResponse;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<User> addUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Other api routes

}
```
 * Here ***ApiResponse*** is a custom class, that we are using to send our respose to client i.e., frontend
```java
package com.ApiResponse;

public class ApiResponse<T> {
    /* code(-1) -> Error or operation failed
       code(0) -> Warning
       code(1) -> Success
     */
    private int code;
    private String message;
    private T data;
    
    public ApiResponse(int code, String message, T data) {
        // Do something
    }
    
    // Generate getters and setters
}
```

## Token based authentication using JWT
* Required dependencies (Add below lines of code in pom.xml)
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
</dependency>
```
* Firstly, it is important to understand that how an api request is passing through different stages i.e., *workflow*
> 1. As we are using ***Spring boot security*** feature, by default it checks authentication for every API request.
> <br>So first we are going to configure that security check
```java
package com.manager.config;
// Import required packages
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private TokenService tokenService;
    
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Here we are allowing api requests with paths ["/", "/api/auth/**"] to bypass without any authentication. All other requests need to be authenticated.
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/", "/api/auth/**").permitAll()
                .anyRequest().authenticated()
        );
        // Disabling CSRF
        http.csrf(CsrfConfigurer::disable);
        // Default settings to show login form for routes which required authentication, but we have to need with it.
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        // For all the routes that need to authenticated we are going to check is user is authorized or not with ***Token***
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenService);
    }
}
```
2. When user visits any route that needs authentication then ***SecurityFilterChain.tokenAuthenticationFilter()*** method is called.
3. Then user token is being checked and if it is valid we will proceed with client request or we gonna reject it
```java
package com.manager.config;
// Import required libraries
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    // Service class for JWT token creating, validation and for extracting payload
    private final TokenService tokenService;

    public TokenAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    // This method is automatically triggered
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Token is fetched from request header 
        String token = getTokenFromRequest(request);
        // If token is valid we are going to authenticate user with default privileges as we don't have user specific privileges
        if (token != null && tokenService.isTokenValid(token)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(null, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing token");
        }
    }
    // Extract token from request header
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```


<br>*To be continued...*