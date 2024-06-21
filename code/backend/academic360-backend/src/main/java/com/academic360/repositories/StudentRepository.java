package com.academic360.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academic360.models.StudentModel;

@Repository
public interface StudentRepository extends JpaRepository<StudentModel, Long> {

    StudentModel findByEmail(String email);

    List<StudentModel> findByStream(String stream);

    List<StudentModel> findByCourse(String course);

    StudentModel findByRollNumberOrRegistrationNumberOrUid(String rollNumber, String registrationNumber, String uid);

}
