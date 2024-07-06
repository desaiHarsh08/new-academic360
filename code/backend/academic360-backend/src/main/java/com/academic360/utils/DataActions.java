package com.academic360.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.academic360.constants.CourseType;
import com.academic360.constants.StreamType;
import com.academic360.constants.SubjectStatus;
import com.academic360.constants.SubjectType;
import com.academic360.dtos.MarksheetDto;
import com.academic360.dtos.SubjectDto;
import com.academic360.exceptions.ResourceNotFoundException;
import com.academic360.models.StudentModel;
import com.academic360.models.SubjectMetadataModel;
import com.academic360.repositories.SubjectMetadataRepository;

public class DataActions {

    @Autowired
    public static SubjectMetadataRepository subjectMetadataRepository;

    public static List<SubjectDto> getSubjectDtoList(List<StudentRow> studentEntries, MarksheetDto marksheetDto) {
        // Fetch all the subject_metadatas
        List<SubjectMetadataModel> subjectMetadataModels = subjectMetadataRepository
                .findByStreamAndCourseAndSemester(
                        studentEntries.get(0).getStream().toUpperCase(),
                        studentEntries.get(0).getCourse().toUpperCase(),
                        studentEntries.get(0).getSemester());

        // Add the subjects
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        List<String> subjectsDone = new ArrayList<>();
        for (StudentRow studentRow : studentEntries) {
            // Skip if subject is duplicate
            if (subjectsDone.contains(studentRow.getSubject())) {
                continue;
            }
            SubjectMetadataModel subjectMetadataModel;
            try {
                
                subjectMetadataModel = subjectMetadataModels.stream()
                        .filter(s -> {
                            String subjectMetadataName = s.getSubjectName().replace(".", "").replace("-", "");
                            String givenSubjectName = studentRow.getSubject().replace(".", "").replace("-", "");

                            return givenSubjectName.equalsIgnoreCase(subjectMetadataName);
                        })
                        .findFirst()
                        .orElse(null);

                if (subjectMetadataModel == null) {
                    throw new IllegalArgumentException("Invalid subject name...");
                }
               
                Float fullMarks = formatFullMarks(studentRow);
                Float internalMarks = formatInternalMarks(studentRow);
                Float practicalMarks = formatPracticalMarks(studentRow);
                Float theoryMarks = formatTheoryMarks(studentRow);
                Float total = formatTotal(studentRow);

                Float ngp = (total != null) ? total / 10 : null;

                String status = null;

                String letterGrade = studentRow.getGrade();

                Float percentageMarks = null;
                if (fullMarks != null && total !=  null) {
                    percentageMarks = (total * 100) / fullMarks;
                    letterGrade = getLetterGrade(percentageMarks);
                    status = getStatus(percentageMarks, marksheetDto);
                }

                SubjectDto subjectDto = new SubjectDto(
                        null,
                        marksheetDto.getId(), // Need to be set in StudentServicesImpl
                        subjectMetadataModel.getSubjectType(),
                        subjectMetadataModel.getSubjectName(),
                        subjectMetadataModel.getCredit(),
                        subjectMetadataModel.getFullMarks(),
                        total,
                        status,
                        letterGrade,
                        ngp,
                        studentRow.getTgp(),
                        internalMarks,
                        practicalMarks,
                        theoryMarks
                    );

                subjectDtoList.add(subjectDto);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return subjectDtoList;
    }

    public static SubjectMetadataModel getSubjectMetadataModel(List<SubjectMetadataModel> subjectMetadataModels, String subjectName) {
        for (SubjectMetadataModel subjectMetadataModel : subjectMetadataModels) {
            if (subjectMetadataModel.getSubjectName().equals(subjectName.toUpperCase())) {
                return subjectMetadataModel;
            }
        }

        throw new ResourceNotFoundException("No subject_metadata exist for the given subjects");
    }

    public static Float formatPracticalMarks(StudentRow studentRow) {
        if ((studentRow.getStream().toUpperCase().equals(StreamType.BA.name()))
                || (studentRow.getStream().toUpperCase().equals(StreamType.BSC.name()))) { // For BA & BSC
        
            try {
                return Float.valueOf(studentRow.getYear2());
            } catch (Exception e) {
                return null;
            }
        }

        return null; // For BCOM, BBA, MA, MCOM
    }

    public static Float calculateSGPA(MarksheetDto marksheetDto) {
        Float ngp_credit = 0F;
        if (marksheetDto.getTotalCredit() == null) {
            return null;
        }

        for (SubjectDto subjectDto: marksheetDto.getSubjects()) {
            if (subjectDto.getNgp() == null || subjectDto.getStatus().equals(SubjectStatus.FAIL.name())) {
                return null;
            }

            ngp_credit += subjectDto.getNgp() + subjectDto.getCredit();
        }

        return (ngp_credit / marksheetDto.getTotalCredit());
    }

    public static Float formatInternalMarks(StudentRow studentRow) {
        if (studentRow.getInternal_marks() != null) {
            return Float.valueOf(studentRow.getInternal_marks());
        }

        return null;
    }

    public static Float formatTheoryMarks(StudentRow studentRow) {;
        if (studentRow.getTheory_marks() != null) {
            try {
                return Float.valueOf(studentRow.getTheory_marks());
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public static Float formatFullMarks(StudentRow studentRow) {
        if (studentRow.getFull_marks() != null) {
            try {
                return Float.valueOf(studentRow.getTotal());
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public static Float formatTotal(StudentRow studentRow) {
        if (studentRow.getTotal() != null) {
            try {
                return Float.valueOf(studentRow.getTotal());
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public static String getLetterGrade(Float percentageMarks) {
        if (percentageMarks >= 90 && percentageMarks <= 100) {
            return "A++";
        } else if (percentageMarks >= 80 && percentageMarks < 90) {
            return "A+";
        } else if (percentageMarks >= 70 && percentageMarks < 80) {
            return "A";
        } else if (percentageMarks >= 60 && percentageMarks < 70) {
            return "B+";
        } else if (percentageMarks >= 50 && percentageMarks < 60) {
            return "B";
        } else if (percentageMarks >= 40 && percentageMarks < 50) {
            return "C+";
        } else if (percentageMarks >= 30 && percentageMarks < 40) {
            return "C";
        } else if (percentageMarks >= 0 && percentageMarks < 30) {
            return "F";
        } else {
            throw new IllegalArgumentException("Invalid percentage marks...");
        }
    }

    public static String getStatus(Float percentageMarks, MarksheetDto marksheetDto) {
        // Check if all subjects got cleared, if not return "Semester not cleared."
        for (SubjectDto subjectDto : marksheetDto.getSubjects()) {
            if (subjectDto.getStatus().equals(SubjectStatus.FAIL.name()) || percentageMarks < 30) {
                return SubjectStatus.FAIL.name();
            }
        }
        
        return SubjectStatus.PASS.name();
    }

    public static String getRemarks(MarksheetDto marksheetDto, StudentModel studentModel) {
        // Check if all subjects got cleared, if not return "Semester not cleared."
        for (SubjectDto subjectDto: marksheetDto.getSubjects()) {
            if (subjectDto.getStatus().equals(SubjectStatus.FAIL.name())) {
                return "Semester not cleared.";
            }
        }
    
        // Get the remarks by the marksheetPercentage
        if(marksheetDto.getPercentageScore() < 30) { // For failed students
            return "Semester not cleared";
        }
        else { // For passed students
            if(marksheetDto.getSemester() != 6) { // For Semester: 1, 2, 3, 4, 5
                return "Semester cleared.";
            }
            else { // For Semester: 6
                if(!studentModel.getStream().equals(StreamType.BCOM.name())) { // For Stream: BA, BSC
                    return "Qualified with Honours.";
                }
                else { // For Stream: BCOM
                    if(studentModel.getCourse().equals(CourseType.HONOURS.name())) { // For honours
                        return "Semester cleared with honours.";
                    }
                    else { // For general
                        return "Semester cleared with general.";
                    }
                }
            }
        }
    
    }

    public static Float getCGPA(List<MarksheetDto> marksheetDtoList) {
        Float sgpaTotalCredit = 0.0F, creditSumAllSem = 0.0F;
        for (int sem = 1; sem <= 6; sem++) {
            final int currentSem = sem; // Create a final variable for use inside the lambda

            MarksheetDto marksheetDto = marksheetDtoList.stream().filter(m -> m.getSemester().equals(currentSem))
                    .findFirst().orElse(null);

            if (marksheetDto == null) {
                return null;
            }

            creditSumAllSem += marksheetDto.getTotalCredit();

            // Find the sum of product of (SGPA * Total_Credit)
            if (marksheetDto.getSgpa() != null && marksheetDto.getTotalCredit() != null) {
                sgpaTotalCredit += marksheetDto.getSgpa() * marksheetDto.getTotalCredit();
            }
        }

        // Return the cgpa
        return (sgpaTotalCredit / creditSumAllSem);
    }

    public static String getClassification(MarksheetDto marksheetDto) {
        // Check if all subjects got cleared, if not return "Previous semester not cleared."
        for (SubjectDto subjectDto: marksheetDto.getSubjects()) {
            if (subjectDto.getStatus().equals(SubjectStatus.FAIL.name())) {
                return "Previous semester not cleared.";
            }
        }
    
        // Return the classification based on the CGPA
        if (marksheetDto.getCgpa() != null) {
            if (marksheetDto.getCgpa() >= 9 && marksheetDto.getCgpa() <= 10) {
                return "Outstanding";
            }
            else if (marksheetDto.getCgpa() >= 8 && marksheetDto.getCgpa() < 9) {
                return "Excellent";
            }
            else if (marksheetDto.getCgpa() >= 7 && marksheetDto.getCgpa() < 8) {
                return "Very Good";
            }
            else if (marksheetDto.getCgpa() >= 6 && marksheetDto.getCgpa() < 7) {
                return "Good";
            }
            else if (marksheetDto.getCgpa() >= 5 && marksheetDto.getCgpa() < 6) {
                return "Average";
            }
            else if (marksheetDto.getCgpa() >= 4 && marksheetDto.getCgpa() < 5) {
                return "Fair";
            }
            else if (marksheetDto.getCgpa() >= 3 && marksheetDto.getCgpa() < 4) {
                return "Satisfactory";
            }
            else if (marksheetDto.getCgpa() >= 0 && marksheetDto.getCgpa() < 3) {
                return "Fail";
            }
        }

        return null;
        
    }

}
