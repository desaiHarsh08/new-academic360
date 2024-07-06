    package com.academic360.models;

    import com.academic360.constants.ModelConstants;

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
    @Table(name = ModelConstants.MARKSHEET_TABLE)
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class MarksheetModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Integer semester;

        @Column(nullable = false)
        private Integer yearOfAppearance;

        @Column(nullable = true)
        private Integer yearOfPassing;

        @Column(nullable = true)
        private Float sgpa;

        @Column(nullable = false)
        private Float totalMarksObtained;

        @Column(nullable = false)
        private Float totalMarksPossible;

        @Column(nullable = false)
        private Float percentageScore;

        @Column(nullable = false)
        private Float totalCredit;

        @Column(nullable = true)
        private String remarks;

        @ManyToOne(targetEntity = StudentModel.class)
        @JoinColumn(name = "student_id_fk", nullable = false)
        private StudentModel student;

        @Column(nullable = true)
        private Float cgpa;

        @Column(nullable = false)
        private String classification;

    }
