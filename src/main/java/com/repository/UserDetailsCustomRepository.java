package com.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsCustomRepository {
    @Autowired
    private SessionFactory sessionFactory;


}
