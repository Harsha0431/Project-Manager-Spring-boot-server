package com.repository;

import com.ApiResponse.ApiResponse;
import com.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
            return new ApiResponse<User>(1, "User registered successfully", user);
        }
        catch (Exception e){
            if(session.getTransaction().isActive())
                session.getTransaction().rollback();
            System.out.println("Caught exception in custom user repository: " + e.getMessage());
        }
        finally {
            session.close();
        }
        return new ApiResponse<User>(-1, "", null);
    }
}
