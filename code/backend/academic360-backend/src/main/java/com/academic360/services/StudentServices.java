package com.academic360.services;

import java.util.List;

import com.academic360.dtos.StudentDto;
import com.academic360.utils.StudentListResponse;

public interface StudentServices {

    StudentDto addStudent(StudentDto studentDto);

    Boolean addMultipleStudents(List<StudentDto> studentDto, String fileName);

    StudentListResponse getAllStudents();

    StudentListResponse getStudentsByStream(String givenStream);

    StudentListResponse getStudentsByCourse(String givenCourse);

    StudentDto getStudentByEmail(String givenEmail);

    StudentDto getStudentByRollNumberOrRegistrationNumberOrUid(String givenSearchValue);

    StudentDto updateStudent(StudentDto givenStudentDto, Long studentId);

    Boolean deleteStudent(Long studentId);

}
