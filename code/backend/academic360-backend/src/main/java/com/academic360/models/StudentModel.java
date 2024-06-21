package com.academic360.models;

import com.academic360.constants.CourseType;
import com.academic360.constants.ModelConstants;
import com.academic360.constants.StreamType;

import jakarta.persistence.Column;
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
@Table(name = ModelConstants.USER_TABLE)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = false)
    private String stream = StreamType.BA.name();

    @Column(nullable = false)
    private String course = CourseType.HONOURS.name();

    @Column(unique = true, nullable = false)
    private String rollNumber;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    @Column(unique = true, nullable = true)
    private String uid;

}
