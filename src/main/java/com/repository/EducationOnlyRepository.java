package com.repository;

import com.model.SchoolEducation;
import com.model.User;
import com.model.UserEducation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationOnlyRepository extends CrudRepository<UserEducation, Long> {
    List<UserEducation> findByUser(User user);
}
