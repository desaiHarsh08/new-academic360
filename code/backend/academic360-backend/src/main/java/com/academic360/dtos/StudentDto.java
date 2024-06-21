package com.academic360.dtos;

import java.util.List;

import com.academic360.constants.CourseType;
import com.academic360.constants.StreamType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private Long id;

    private String name;

    private String email;

    private String profileImage;

    private String phone;

    private String stream = StreamType.BA.name();

    private String course = CourseType.HONOURS.name();

    private String rollNumber;

    private String registrationNumber;

    private String uid;

    List<MarksheetDto> marksheets;

}
