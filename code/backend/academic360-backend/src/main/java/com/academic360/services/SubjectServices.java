package com.academic360.services;

import java.util.List;

import com.academic360.dtos.SubjectDto;

public interface SubjectServices {

    SubjectDto addSubject(SubjectDto givenSubjectDto);

    List<SubjectDto> getSubjectsForMarksheet(Long marksheetId);

    List<SubjectDto> getSubjectsByType(String givenSubjectType);

    SubjectDto getSubjectById(Long subjectId);

    SubjectDto updateSubject(SubjectDto givenSubjectDto);

    Boolean deleteSubject(Long subjectId);

}
