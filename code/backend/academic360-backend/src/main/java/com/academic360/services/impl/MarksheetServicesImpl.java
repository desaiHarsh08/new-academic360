package com.academic360.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academic360.constants.StreamType;
import com.academic360.dtos.MarksheetDto;
import com.academic360.dtos.SubjectDto;
import com.academic360.exceptions.ResourceNotFoundException;
import com.academic360.models.MarksheetModel;
import com.academic360.models.StudentModel;
import com.academic360.repositories.MarksheetRepository;
import com.academic360.repositories.StudentRepository;
import com.academic360.services.MarksheetServices;
import com.academic360.services.SubjectServices;
import com.academic360.utils.DataActions;
import com.academic360.utils.ListResponse;
import com.academic360.utils.StudentRow;

@Service
public class MarksheetServicesImpl implements MarksheetServices {

    private static int PAGE_SIZE = 50;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MarksheetRepository marksheetRepository;

    @Autowired
    private SubjectServices subjectServices;

    @Override
    public MarksheetDto createMarksheetDtoByStudentEntries(List<StudentRow> studentEntries, StudentModel studentModel) {
        MarksheetDto marksheetDto = new MarksheetDto();

        // Set the student
        marksheetDto.setStudentId(studentModel.getId());
        // Set the semester
        marksheetDto.setSemester(studentEntries.get(0).getSemester());
        // Set the year of appearance
        marksheetDto.setYearOfAppearance(studentEntries.get(0).getYear1());
        // Set the year of passing
        Integer yearOfPassing = studentEntries.get(0)
                .getStream().toUpperCase() == StreamType.BCOM.name() ? studentEntries.get(0).getYear2() : null;
        marksheetDto.setYearOfPassing(yearOfPassing);

        // Create the subjects
        List<SubjectDto> subjectDtoList = DataActions.getSubjectDtoList(studentEntries, marksheetDto);

        // Set the subjects
        marksheetDto.setSubjects(subjectDtoList);

        // Calculate the totalMarksObtained, totalMarksPossible, percentageScore, totalCredit, remarks
        // 1. Initialize variables
        Float totalMarksObtained = 0F;
        Float totalMarksPossible = 0F;
        Float totalCredit = 0F;
        // 2. Calculate totals
        for (SubjectDto subjectDto : subjectDtoList) {
            if (subjectDto.getTotal() != null) {
                totalMarksObtained = (totalMarksObtained != null) ? totalMarksObtained + subjectDto.getTotal() : null;
            }

            if (subjectDto.getFullMarks() != null) {
                totalMarksPossible = (totalMarksPossible != null) ? totalMarksPossible + subjectDto.getFullMarks()
                        : null;
            }

            if (subjectDto.getCredit() != null) {
                totalCredit = (totalCredit != null) ? totalCredit + subjectDto.getCredit() : null;
            }
        }

        // Set calculated values back to the marksheetDto
        marksheetDto.setTotalMarksObtained(totalMarksObtained);
        marksheetDto.setTotalMarksPossible(totalMarksPossible);
        marksheetDto.setTotalCredit(totalCredit);

        // Set sgpa for the marksheet
        marksheetDto.setSgpa(DataActions.calculateSGPA(marksheetDto));

        // Calculate percentageScore and remarks if necessary
        if (totalMarksObtained != null && totalMarksPossible != null) {
            marksheetDto.setPercentageScore((totalMarksObtained / totalMarksPossible) * 100);
        } else {
            marksheetDto.setPercentageScore(null);
        }

        marksheetDto.setRemarks(DataActions.getRemarks(marksheetDto, studentModel));

        // Calculate the cgpa and set classifications
        if (marksheetDto.getSemester() == 6) {
            List<MarksheetDto> marksheetDtoList = this.marksheetModelListToDtoList(this.marksheetRepository.findByStudent(studentModel));
            marksheetDto.setCgpa(DataActions.getCGPA(marksheetDtoList));
            marksheetDto.setClassification(DataActions.getClassification(marksheetDto));

        }

        // Return the marksheetDto
        return marksheetDto;
    }

    @Override
    public ListResponse<List<MarksheetDto>> getAllMarksheets(int pageNumber) { // Default pageNumber is 1
        if (pageNumber == 0) {
            throw new IllegalArgumentException("Page number can't be less than 1.");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        
        Page<MarksheetModel> marksheetPage = this.marksheetRepository.findAll(pageable);

        List<MarksheetModel> marksheetModelList = marksheetPage.getContent();

        // Prepare the response
        ListResponse<List<MarksheetDto>> listResponse = new ListResponse(
            pageNumber,
            PAGE_SIZE,
            marksheetPage.getTotalPages(),
            marksheetPage.getTotalElements(),
            this.marksheetModelListToDtoList(marksheetModelList)
        );

        return listResponse;
    }

    @Override
    public ListResponse<List<MarksheetDto>> getMarksheetsBySemester(Integer givenSemester, int pageNumber) {
        if (pageNumber == 0) {
            throw new IllegalArgumentException("Page number can't be less than 1.");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        
        Page<MarksheetModel> marksheetPage = this.marksheetRepository.findBySemester(givenSemester, pageable);

        List<MarksheetModel> marksheetModelList = marksheetPage.getContent();

        // Prepare the response
        ListResponse<List<MarksheetDto>> listResponse = new ListResponse(
            pageNumber,
            PAGE_SIZE,
            marksheetPage.getTotalPages(),
            marksheetPage.getTotalElements(),
            this.marksheetModelListToDtoList(marksheetModelList)
        );

        return listResponse;
    }

    @Override
    public ListResponse<List<MarksheetDto>> getMarksheetsByYearOfAppearance(Integer givenYearOfAppearance, int pageNumber) {
        if (pageNumber == 0) {
            throw new IllegalArgumentException("Page number can't be less than 1.");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);
        
        Page<MarksheetModel> marksheetPage = this.marksheetRepository.findByYearOfAppearance(givenYearOfAppearance, pageable);

        List<MarksheetModel> marksheetModelList = marksheetPage.getContent();

        // Prepare the response
        ListResponse<List<MarksheetDto>> listResponse = new ListResponse(
            pageNumber,
            PAGE_SIZE,
            marksheetPage.getTotalPages(),
            marksheetPage.getTotalElements(),
            this.marksheetModelListToDtoList(marksheetModelList)
        );

        return listResponse;
    }

    @Override
    public List<MarksheetDto> getMarksheetsForStudent(Long givenStudentId) {
        StudentModel foundStudentModel = this.studentRepository.findById(givenStudentId).orElse(null);
        if (foundStudentModel == null) {
            throw new ResourceNotFoundException("No student exist for id: " + givenStudentId);
        }

        List<MarksheetModel> marksheetModelList = this.marksheetRepository.findByStudent(foundStudentModel);

        return this.marksheetModelListToDtoList(marksheetModelList);
    }

    @Override
    public MarksheetDto getMarksheetById(Long givenMarksheetId) {
        MarksheetModel foundMarksheet = this.marksheetRepository.findById(givenMarksheetId).orElseThrow(
            () -> new ResourceNotFoundException("No marksheet exist for id: " + givenMarksheetId)
        );

        return this.marksheetModelToDto(foundMarksheet);
    }

    @Override
    public MarksheetDto updateMarksheet(MarksheetDto givenMarksheetDto) {
        MarksheetModel foundMarksheet = this.marksheetRepository.findById(givenMarksheetDto.getId()).orElseThrow(
            () -> new ResourceNotFoundException("No marksheet exist for id: " + givenMarksheetDto.getId())
        );

        StudentModel foundStudent = this.studentRepository.findById(foundMarksheet.getStudent().getId()).orElseThrow(
            () -> new ResourceNotFoundException("No student exist for id: " + givenMarksheetDto.getStudentId())
        );

        foundMarksheet.setStudent(foundStudent);

        // Update the subjects
        for (SubjectDto subjectDto: givenMarksheetDto.getSubjects()) {
            this.subjectServices.updateSubject(subjectDto);
        }

        // TODO: Update the marksheet
        foundMarksheet = this.marksheetDtoToModel(givenMarksheetDto);

        // Save the marksheet
        this.marksheetRepository.save(foundMarksheet);

        return this.getMarksheetById(givenMarksheetDto.getId());
    }

    @Override
    public Boolean deleteMarksheet(Long marksheetId) {
        MarksheetDto foundMarksheet = this.getMarksheetById(marksheetId);

        // Delete all the subjects
        for (SubjectDto subjectDto: foundMarksheet.getSubjects()) {
            this.subjectServices.deleteSubject(subjectDto.getId());
        }

        // Delete the marksheet
        this.marksheetRepository.deleteById(marksheetId);

        throw new UnsupportedOperationException("Unimplemented method 'deleteMarksheet'");
    }

    public MarksheetDto marksheetModelToDto(MarksheetModel marksheetModel) {
        if (marksheetModel == null) {
            return null;
        }

        MarksheetDto marksheetDto = this.modelMapper.map(marksheetModel, MarksheetDto.class);
        
        marksheetDto.setStudentId(marksheetModel.getStudent().getId());

        marksheetDto.setSubjects(this.subjectServices.getSubjectsForMarksheet(marksheetDto.getId()));

        return marksheetDto;
    }

    public List<MarksheetDto> marksheetModelListToDtoList(List<MarksheetModel> marksheetModelList) {
        if (marksheetModelList == null) {
            return new ArrayList<>();
        }
        List<MarksheetDto> marksheetDtoList = new ArrayList<>();

        for (MarksheetModel marksheetModel: marksheetModelList) {
            marksheetDtoList.add(this.marksheetModelToDto(marksheetModel));
        }

        return marksheetDtoList;
    }


    public MarksheetModel marksheetDtoToModel(MarksheetDto marksheetDto) {
        return this.modelMapper.map(marksheetDto, MarksheetModel.class);
    }
    
    public List<MarksheetModel> marksheetDtoListToModelList(List<MarksheetDto> marksheetDtoList) {
        if (marksheetDtoList == null) {
            return new ArrayList<>();
        }

        List<MarksheetModel> marksheetModelList = new ArrayList<>();
        for (MarksheetDto marksheetDto: marksheetDtoList) {
            marksheetModelList.add(this.marksheetDtoToModel(marksheetDto));
        } 

        return marksheetModelList;
    }

}
