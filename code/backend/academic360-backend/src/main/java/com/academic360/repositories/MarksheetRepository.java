package com.academic360.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academic360.models.MarksheetModel;
import com.academic360.models.StudentModel;

@Repository
public interface MarksheetRepository extends JpaRepository<MarksheetModel, Long> {

    List<MarksheetModel> findBySemester(Integer semester);

    List<MarksheetModel> findByYearOfAppearance(Integer yearOfAppearance);

    List<MarksheetModel> findByYearOfPassing(Integer yearOfPassing);

    List<MarksheetModel> findByStudent(StudentModel student);

}
