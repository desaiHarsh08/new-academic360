package com.academic360.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academic360.models.MarksheetModel;
import com.academic360.models.StudentModel;

@Repository
public interface MarksheetRepository extends JpaRepository<MarksheetModel, Long> {

    Page<MarksheetModel> findBySemester(Integer semester, Pageable pageable);

    Page<MarksheetModel> findByYearOfAppearance(Integer yearOfAppearance, Pageable pageable);

    Page<MarksheetModel> findByYearOfPassing(Integer yearOfPassing, Pageable pageable);

    List<MarksheetModel> findByStudent(StudentModel student);

}
