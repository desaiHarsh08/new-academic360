package com.academic360.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academic360.models.SubjectMetadataModel;
import java.util.List;


public interface SubjectMetadataRepository extends JpaRepository<SubjectMetadataModel, Long> {

    List<SubjectMetadataModel> findByStream(String stream);

    List<SubjectMetadataModel> findByCourse(String course);

    List<SubjectMetadataModel> findBySemester(Integer semester);

    List<SubjectMetadataModel> findByStreamAndCourseAndSemester(String stream, String course, Integer semester);

    SubjectMetadataModel findByStreamAndSubjectTypeAndSemesterAndSubjectName(String stream, String subjectType, Integer semester, String subjectName);

    List<SubjectMetadataModel> findBySubjectType(String subjectType);


}
