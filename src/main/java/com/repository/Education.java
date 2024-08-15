package com.repository;

import com.model.UserEducation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Delete this
@Repository
public interface Education extends CrudRepository<UserEducation, String> {
}
