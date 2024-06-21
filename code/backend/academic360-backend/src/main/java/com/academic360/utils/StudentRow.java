package com.academic360.utils;

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
public class StudentRow {

    private Integer id;

    private String registration_no;

    private String stream;

    private String course;

    private Integer semester;

    private String name;

    private Double sgpa;

    private String remarks;

    private Float fullMarks;

    private Integer year1;

    private Integer year2;

    private Float ngp;

    private Integer credit;

    private Float tgp;

    private String subject;

    private Float internal_marks;

    private Float theory_marks;

    private Float total;

    private String status;

    private String grade;

    private String roll_no;

    // Additional constructor for specific use cases
    public StudentRow(String registration_no, String stream, String course) {
        this.registration_no = registration_no;
        this.stream = stream;
        this.course = course;
    }

}
