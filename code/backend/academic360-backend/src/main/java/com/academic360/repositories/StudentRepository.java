package com.academic360.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.academic360.models.StudentModel;

@Repository
public interface StudentRepository extends JpaRepository<StudentModel, Long> {

    StudentModel findByEmail(String email);

    Page<StudentModel> findByStream(String stream, Pageable pageable);

    Page<StudentModel> findByCourse(String course, Pageable pageable);

    @Query("SELECT s FROM StudentModel s WHERE s.rollNumber = :searchValue OR s.registrationNumber = :searchValue OR s.uid = :searchValue")
    StudentModel findByRollNumberOrRegistrationNumberOrUid(@Param("searchValue") String searchValue);

}
