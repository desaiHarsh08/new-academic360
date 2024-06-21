package com.academic360.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academic360.dtos.StudentDto;
import com.academic360.services.StudentServices;
import com.academic360.utils.FileService;
import com.academic360.utils.StudentListResponse;
import com.academic360.utils.StudentRow;

@Service
public class StudentServicesImpl implements StudentServices {

    public static final String[] stream = {"BA", "BSC", "BCOM", "BBA", "M.A", "M.COM"};

    @Autowired
    private FileService fileService;

    @Override
    public StudentDto addStudent(StudentDto studentDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addStudent'");
    }

    @Override
    public Boolean addMultipleStudents(List<StudentDto> studentDto, String fileName) {
        Boolean isAllStudentsAdded = false;
        // Read the uploaded file
        List<StudentRow> givenStudentList = this.fileService.readFile(fileName);
        // Sort the students by year of appearance (i.e column 'year1' as mentioned in the file)
        // Sort the students by semester
        // Sort the students by stream
        // Sort the students by course
        // Add the student, if not exist (search by rollNumber, registrationNumber or uid)
        // Create the marksheet

        isAllStudentsAdded = true;

        return isAllStudentsAdded;
    }

    @Override
    public StudentListResponse getAllStudents() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllStudents'");
    }

    @Override
    public StudentListResponse getStudentsByStream(String givenStream) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentsByStream'");
    }

    @Override
    public StudentListResponse getStudentsByCourse(String givenCourse) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentsByCourse'");
    }

    @Override
    public StudentDto getStudentByEmail(String givenEmail) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentByEmail'");
    }

    @Override
    public StudentDto getStudentByRollNumberOrRegistrationNumberOrUid(String givenSearchValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentByRollNumberOrRegistrationNumberOrUid'");
    }

    @Override
    public StudentDto updateStudent(StudentDto givenStudentDto, Long studentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStudent'");
    }

    @Override
    public Boolean deleteStudent(Long studentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteStudent'");
    }

}
