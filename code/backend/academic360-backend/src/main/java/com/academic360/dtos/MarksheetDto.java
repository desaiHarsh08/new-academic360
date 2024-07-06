package com.academic360.dtos;

import java.util.List;

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
public class MarksheetDto {

    private Long id;

    private Integer semester;

    private Integer yearOfAppearance;

    private Integer yearOfPassing;

    private Float sgpa;

    private Float totalMarksObtained;

    private Float totalMarksPossible;

    private Float percentageScore;

    private Float totalCredit;

    private String remarks;

    private Long studentId;

    List<SubjectDto> subjects;

    private Float cgpa;

    private String classification;

}
