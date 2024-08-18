package com.repository;

import com.model.UserDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {
    @Query("select ud from UserDetails ud join fetch ud.user u where u.email=:email")
    UserDetails getUserDetailsWithEmail(@Param("email") String email);
}
