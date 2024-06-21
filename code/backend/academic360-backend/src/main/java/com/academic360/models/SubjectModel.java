package com.academic360.models;

import com.academic360.constants.ModelConstants;
import com.academic360.constants.SubjectStatus;
import com.academic360.constants.SubjectType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = ModelConstants.SUBJECT_TABLE)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubjectModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = MarksheetModel.class)
    @JoinColumn(name = "marksheet_id_fk")
    private MarksheetModel marksheet;

    @Column(nullable = false)
    private String subjectType = SubjectType.COMMON.name();

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private Integer credit;

    @Column(nullable = false)
    private Float fullMarks;

    @Column(nullable = false)
    private Float total;

    @Column(nullable = false)
    private String status = SubjectStatus.FAIL.name();

    @Column(nullable = false)
    private String letterGrade;

    @Column(nullable = false)
    private Float ngp;

    @Column(nullable = false)
    private Float tgp;

    @Column(nullable = false)
    private Float internalMarks;

    @Column(nullable = false)
    private Float practicalMarks;

    @Column(nullable = false)
    private Float theoryMarks;

}
