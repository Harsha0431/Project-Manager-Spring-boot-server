package com.repository;

import com.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
/*
* The @Repository annotation is a marker for any class that fulfils the role or stereotype of a repository (also known as Data Access Object or DAO).
* Among the uses of this marker is the automatic translation of exceptions, as described in Exception Translation.
* */
public interface UserRepository extends CrudRepository<User, String> {
}
