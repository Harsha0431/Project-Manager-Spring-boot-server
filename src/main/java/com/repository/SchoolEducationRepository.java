package com.repository;

import com.model.SchoolEducation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolEducationRepository extends CrudRepository<SchoolEducation, Long> {
}
