package com.academic360.services;

import java.util.List;

import com.academic360.dtos.SubjectDto;
import com.academic360.models.SubjectModel;

public interface SubjectServices {

    SubjectDto addSubject(SubjectDto givenSubjectDto);

    List<SubjectDto> getSubjectsForMarksheet(Long marksheetId);

    List<SubjectDto> getSubjectsByType(String givenSubjectType);

    SubjectDto getSubjectById(Long subjectId);

    SubjectDto updateSubject(SubjectDto givenSubjectDto);

    Boolean deleteSubject(Long subjectId);

    public SubjectModel subjectDtoToModel(SubjectDto subjectDto);

    public List<SubjectModel> subjectDtoListToModelList(List<SubjectDto> subjectDtoList);

}
