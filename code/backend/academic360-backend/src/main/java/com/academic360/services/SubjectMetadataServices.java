package com.academic360.services;

import java.util.List;

import com.academic360.models.SubjectMetadataModel;

public interface SubjectMetadataServices {

    public SubjectMetadataModel createSubjectMetadata(SubjectMetadataModel subjectMetadataModel);

    public List<SubjectMetadataModel> getAllSubjects();

    public List<SubjectMetadataModel> getSubjectsByStream(String givenStream);

    public List<SubjectMetadataModel> getSubjectsByCourse(String givenCourse);

    public SubjectMetadataModel getSubjectByStreamSubjectTypeSemesterAndSubjectName(String givenStream,
            String givenSubjectType, Integer givenSemester, String givenSubjectName);

    public List<SubjectMetadataModel> getSubjectsByType(String givenSubjectType);

    public SubjectMetadataModel getSubjectMetadataById(Long subjectMetadataId);

    public SubjectMetadataModel updateSubject(SubjectMetadataModel givenSubjectMetadataModel);
    
    public Boolean deleteSubject(Long subjectMetadataId);

}
