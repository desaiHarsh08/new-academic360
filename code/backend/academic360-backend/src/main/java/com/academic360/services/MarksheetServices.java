package com.academic360.services;

import java.util.List;

import com.academic360.dtos.MarksheetDto;
import com.academic360.models.MarksheetModel;
import com.academic360.models.StudentModel;
import com.academic360.utils.ListResponse;
import com.academic360.utils.StudentRow;

public interface MarksheetServices {

    MarksheetDto createMarksheetDtoByStudentEntries(List<StudentRow> studentEntries, StudentModel studentModel);

    ListResponse<List<MarksheetDto>> getAllMarksheets(int pageNumber);
    
    ListResponse<List<MarksheetDto>> getMarksheetsBySemester(Integer givenSemester, int pageNumber);
    
    ListResponse<List<MarksheetDto>> getMarksheetsByYearOfAppearance(Integer givenYearOfAppearance, int pageNumber);
    
    List<MarksheetDto> getMarksheetsForStudent(Long givenStudentId);

    MarksheetDto getMarksheetById(Long givenMarksheetId);

    MarksheetDto updateMarksheet(MarksheetDto givenMarksheetDto);

    Boolean deleteMarksheet(Long marksheetId);

    List<MarksheetModel> marksheetDtoListToModelList(List<MarksheetDto> marksheetDtoList);
    
    MarksheetModel marksheetDtoToModel(MarksheetDto marksheetDto);

}
