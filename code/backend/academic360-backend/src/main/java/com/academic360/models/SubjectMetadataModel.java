package com.academic360.models;

import com.academic360.constants.ModelConstants;
import com.academic360.constants.StreamType;
import com.academic360.constants.SubjectType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = ModelConstants.SUBJECT_METADATA_TABLE)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubjectMetadataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stream = StreamType.BA.name();

    private Integer semester;

    private String subjectName;

    private String subjectType = SubjectType.COMMON.name();

    private Float fullMarks;

    private Integer credit;

    private Integer totalSubjects;
    
}
