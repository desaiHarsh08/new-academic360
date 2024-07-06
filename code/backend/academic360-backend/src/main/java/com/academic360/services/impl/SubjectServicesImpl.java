package com.academic360.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academic360.dtos.SubjectDto;
import com.academic360.models.SubjectModel;
import com.academic360.services.SubjectServices;

@Service
public class SubjectServicesImpl implements SubjectServices {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SubjectDto addSubject(SubjectDto givenSubjectDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addSubject'");
    }

    @Override
    public List<SubjectDto> getSubjectsForMarksheet(Long marksheetId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSubjectsForMarksheet'");
    }

    @Override
    public List<SubjectDto> getSubjectsByType(String givenSubjectType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSubjectsByType'");
    }

    @Override
    public SubjectDto getSubjectById(Long subjectId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSubjectById'");
    }

    @Override
    public SubjectDto updateSubject(SubjectDto givenSubjectDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSubject'");
    }

    @Override
    public Boolean deleteSubject(Long subjectId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSubject'");
    }


    public SubjectModel subjectDtoToModel(SubjectDto subjectDto) {
        if (subjectDto == null) {
            return null;
        }

        return this.modelMapper.map(subjectDto, SubjectModel.class);
    }

    public List<SubjectModel> subjectDtoListToModelList(List<SubjectDto> subjectDtoList) {
        if (subjectDtoList == null) {
            return new ArrayList<>();
        }

        List<SubjectModel> subjectModelList = new ArrayList<>();
        for (SubjectDto subjectDto: subjectDtoList) {
            subjectModelList.add(this.subjectDtoToModel(subjectDto));
        }

        return subjectModelList;
    }

}
