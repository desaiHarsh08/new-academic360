package com.academic360.services;

import java.util.List;

import com.academic360.dtos.MarksheetDto;
import com.academic360.utils.MarksheetListResponse;

public interface MarksheetServices {

    MarksheetDto createMarksheet(MarksheetDto marksheetDto);

    MarksheetListResponse getAllMarksheets();
    
    MarksheetListResponse getMarksheetsBySemester(Integer givenSemester);
    
    MarksheetListResponse getMarksheetsByYearOfAppearance(Integer givenYearOfAppearance);
    
    List<MarksheetDto> getMarksheetsForStudent(Long givenStudentId);

    MarksheetDto getMarksheetById(Long givenMarksheetId);

    MarksheetDto updateMarksheet(MarksheetDto givenMarksheetDto);

    Boolean deleteMarksheet(Long marksheetId);

}
