package com.academic360.dtos;

import com.academic360.constants.SubjectStatus;
import com.academic360.constants.SubjectType;

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
public class SubjectDto {

    private Long id;

    private Long marksheetId;

    private String subjectType = SubjectType.COMMON.name();

    private String subjectName;

    private Integer credit;

    private Float fullMarks;

    private Float total;

    private String status = SubjectStatus.FAIL.name();

    private String letterGrade;

    private Float ngp;

    private Float tgp;

    private Float internalMarks;

    private Float practicalMarks;

    private Float theoryMarks;

}
