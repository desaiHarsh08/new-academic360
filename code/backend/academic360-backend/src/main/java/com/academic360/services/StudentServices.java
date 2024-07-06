package com.academic360.services;

import java.util.List;

import com.academic360.dtos.StudentDto;
import com.academic360.utils.ListResponse;

public interface StudentServices {

    StudentDto addStudent(StudentDto studentDto);

    Boolean addMultipleStudents(String fileName);

    ListResponse<List<StudentDto>> getAllStudents(int pageNumber);

    ListResponse<List<StudentDto>> getStudentsByStream(String givenStream, int pageNumber);

    ListResponse<List<StudentDto>> getStudentsByCourse(String givenCourse, int pageNumber);

    StudentDto getStudentById(Long givenStudentId);

    StudentDto getStudentByEmail(String givenEmail);

    StudentDto getStudentByRollNumberOrRegistrationNumberOrUid(String givenSearchValue);

    StudentDto updateStudent(StudentDto givenStudentDto, Long studentId);

    Boolean deleteStudent(Long studentId);

}
