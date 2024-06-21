package com.academic360.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academic360.models.MarksheetModel;
import com.academic360.models.SubjectModel;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectModel, Long> {

    List<SubjectModel> findByMarksheet(MarksheetModel marksheet);

    List<SubjectModel> findBySubjectType(String subjectType);

}
