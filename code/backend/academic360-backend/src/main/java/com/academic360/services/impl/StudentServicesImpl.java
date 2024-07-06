package com.academic360.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academic360.constants.CourseType;
import com.academic360.constants.StreamType;
import com.academic360.dtos.MarksheetDto;
import com.academic360.dtos.StudentDto;
import com.academic360.dtos.SubjectDto;
import com.academic360.exceptions.ResourceNotFoundException;
import com.academic360.models.MarksheetModel;
import com.academic360.models.StudentModel;
import com.academic360.models.SubjectModel;
import com.academic360.repositories.MarksheetRepository;
import com.academic360.repositories.StudentRepository;
import com.academic360.repositories.SubjectRepository;
import com.academic360.services.MarksheetServices;
import com.academic360.services.StudentServices;
import com.academic360.services.SubjectServices;
import com.academic360.utils.FileService;
import com.academic360.utils.ListResponse;
import com.academic360.utils.StudentRow;

@Service
public class StudentServicesImpl implements StudentServices {

    private static int PAGE_SIZE = 50;

    public static final String[] streams = { "BA", "BSC", "BCOM", "BBA", "MA", "MCOM" };

    public static final String[] courses = { "HONOURS", "GENERAL" };

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MarksheetServices marksheetServices;

    @Autowired
    private MarksheetRepository marksheetRepository;

    @Autowired
    private SubjectServices subjectServices;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public StudentDto addStudent(StudentDto studentDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addStudent'");
    }

    @Override
    public Boolean addMultipleStudents(String fileName) {
        try {
            // Read the uploaded file
            List<StudentRow> givenStudentList = this.fileService.readFile(fileName);

            int currentYear = Calendar.getInstance().get(Calendar.YEAR) + 1900;

            // Format stream and course
            givenStudentList = this.formatStreamAndCourse(givenStudentList);

            // Process the data by iterating over it by stream
            for (int streamIndex = 0; streamIndex < streams.length; streamIndex++) {
                // Filter the students by stream
                List<StudentRow> studentsByStream = this.filterStudentRowsByStream(givenStudentList,
                        streams[streamIndex]);

                // Iterate over the `studentsByStream[]` from year: 2010
                for (Integer year = 2010; year <= currentYear; year++) {
                    // Filter the students by year of appearance (i.e column 'year1' as mentioned in
                    // the file
                    List<StudentRow> studentsByYear = this.filterStudentRowsByYear(studentsByStream, year,
                            streams[streamIndex]);

                    // Create the new studentDto list
                    List<StudentDto> studentDtoList = new ArrayList<>();

                    // Iterate over all the semesters (i.e., 1 - 6)
                    for (Integer semester = 1; semester <= 6; semester++) {
                        // Filter the students by semester
                        List<StudentRow> studentsBySemester = this.filterStudentRowsBySemester(studentsByYear,
                                semester);

                        for (Integer courseIndex = 0; courseIndex < courses.length; courseIndex++) {
                            // Filter the students by course: courses[courseIndex]
                            List<StudentRow> studentsByCourse = this.filterStudentRowsByCourse(studentsBySemester,
                                    courses[courseIndex]);
                            // Iterate over the studentsByCourse
                            for (StudentRow studentRow : studentsByCourse) {
                                studentDtoList = this.processStudentRow(studentDtoList, studentsByCourse, studentRow);
                            }
                        }
                        System.out.printf("\nYear(%d)\tSemester(%d)\tStream(%s)\tStudents(%d)", year, semester,
                                streams[streamIndex], studentDtoList.size());
                    }

                    // Do insert data for the `year`
                    this.insertData(studentDtoList);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<StudentRow> formatStreamAndCourse(List<StudentRow> studentRows) {
        return studentRows.stream().map(s -> {
            // Convert the stream and course to uppercase
            s.setStream(s.getStream().toUpperCase());
            s.setCourse(s.getCourse().toUpperCase());

            // Format the stream by removing "."
            if (s.getStream().contains(".")) {
                s.setStream(s.getStream().toUpperCase().replace(".", ""));
            }
            // Check if the stream is valid
            if ((!s.getStream().equals(StreamType.BA.name())) &&
                    (!s.getStream().equals(StreamType.BCOM.name())) &&
                    (!s.getStream().equals(StreamType.BBA.name())) &&
                    (!s.getStream().equals(StreamType.MA.name())) &&
                    (!s.getStream().equals(StreamType.MCOM.name()))) {
                throw new IllegalArgumentException("Invalid stream provided...");
            }

            if ((!s.getCourse().equals(CourseType.GENERAL.name())) &&
                    (!s.getCourse().equals(CourseType.HONOURS.name()))) {
                throw new IllegalArgumentException("Invalid course provided...");
            }

            return s;
        }).collect(Collectors.toList());
    }

    public List<StudentDto> processStudentRow(
            List<StudentDto> studentDtoList, List<StudentRow> studentsByCourse, StudentRow studentRow) {
        StudentDto studentDto = new StudentDto();
        // Get all the student's entries by `roll_no`
        List<StudentRow> studentEntries = this.filterStudentRowsByRollNumber(studentsByCourse,
                studentRow.getRoll_no());

        // Check if the student exist, if not then create new student
        StudentModel foundStudentModel = this.studentRepository
                .findByRollNumberOrRegistrationNumberOrUid(studentRow.getRoll_no());

        if (foundStudentModel == null) { // New Student
            StudentModel studentModel = new StudentModel(
                    null,
                    studentRow.getName(),
                    studentRow.getEmail(),
                    studentRow.getProfileImage(),
                    studentRow.getPhone(),
                    studentRow.getStream().toUpperCase(),
                    studentRow.getCourse(),
                    studentRow.getRoll_no(),
                    studentRow.getRegistration_no(),
                    studentRow.getUid());

            studentDto = this.modelMapper.map(studentModel, StudentDto.class);
        } else { // Found student
            studentDto = this.modelMapper.map(foundStudentModel, StudentDto.class);
        }

        // Create the marksheet
        MarksheetDto marksheetDto = this.marksheetServices.createMarksheetDtoByStudentEntries(
                studentEntries, this.modelMapper.map(studentDto, StudentModel.class));

        // Check if studentDto already exists in `studentDtoList[]` by rollNo
        StudentDto existingStudentDto = studentDtoList.stream()
                .filter(s -> s.getRollNumber().equals(studentRow.getRoll_no()))
                .findFirst()
                .orElse(null);

        if (existingStudentDto == null) { // New Element
            studentDto.getMarksheets().add(marksheetDto);
            studentDtoList.add(studentDto);
        } else { // Add the marksheet to existing studentDto
            existingStudentDto.getMarksheets().add(marksheetDto);
            // Ensure we update the existing student in the list
            int index = studentDtoList.indexOf(existingStudentDto);
            studentDtoList.set(index, existingStudentDto);

        }

        return studentDtoList;
    }

    public void insertData(List<StudentDto> studentDtoList) {
        List<StudentModel> studentModelList = this.studentDtoListToModelList(studentDtoList);

        // Insert the students
        studentModelList = this.studentRepository.saveAll(studentModelList);

        // Create a map of roll number to StudentModel for quick lookup
        Map<String, StudentModel> studentMap = studentModelList.stream()
                .collect(Collectors.toMap(StudentModel::getRollNumber, student -> student));

        // Set the student id to all the marksheets and collect all marksheetDtos
        List<MarksheetDto> marksheetDtoList = new ArrayList<>();
        for (StudentDto studentDto : studentDtoList) {
            StudentModel studentModel = studentMap.get(studentDto.getRollNumber());
            if (studentModel != null) {
                for (MarksheetDto marksheetDto : studentDto.getMarksheets()) {
                    marksheetDto.setStudentId(studentModel.getId());
                    marksheetDtoList.add(marksheetDto);
                }
            }
        }

        // Insert the marksheets
        List<MarksheetModel> marksheetModelList = this.marksheetServices.marksheetDtoListToModelList(marksheetDtoList);
        marksheetModelList = this.marksheetRepository.saveAll(marksheetModelList);

        // Create a map of criteria (studentId, semester, yearOfAppearance,
        // yearOfPassing) to MarksheetModel for quick lookup
        Map<String, MarksheetModel> marksheetMap = marksheetModelList.stream()
                .collect(Collectors.toMap(
                        m -> m.getStudent().getId() + "-" + m.getSemester() + "-" + m.getYearOfAppearance() + "-"
                                + m.getYearOfPassing(),
                        marksheet -> marksheet));

        // Set the marksheet id to all the subjects and collect all subjectDtos
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        for (MarksheetDto marksheetDto : marksheetDtoList) {
            String key = marksheetDto.getStudentId() + "-" + marksheetDto.getSemester() + "-"
                    + marksheetDto.getYearOfAppearance() + "-" + marksheetDto.getYearOfPassing();
            MarksheetModel marksheetModel = marksheetMap.get(key);
            if (marksheetModel != null) {
                for (SubjectDto subjectDto : marksheetDto.getSubjects()) {
                    subjectDto.setMarksheetId(marksheetModel.getId());
                    subjectDtoList.add(subjectDto);
                }
            }
        }

        // Insert the subjects
        List<SubjectModel> subjectModelList = this.subjectServices.subjectDtoListToModelList(subjectDtoList);
        subjectModelList = this.subjectRepository.saveAll(subjectModelList);

    }

    @Override
    public ListResponse<List<StudentDto>> getAllStudents(int pageNumber) {
        if (pageNumber == 0) {
            throw new IllegalArgumentException("Page number can't be less than 1.");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);

        Page<StudentModel> studentPage = this.studentRepository.findAll(pageable);

        List<StudentModel> studentModelList = studentPage.getContent();

        // Prepare the response
        ListResponse<List<StudentDto>> listResponse = new ListResponse(
                pageNumber,
                PAGE_SIZE,
                studentPage.getTotalPages(),
                studentPage.getTotalElements(),
                this.stundentModelListToDtoList(studentModelList));

        return listResponse;
    }

    @Override
    public ListResponse<List<StudentDto>> getStudentsByStream(String givenStream, int pageNumber) {
        if (pageNumber == 0) {
            throw new IllegalArgumentException("Page number can't be less than 1.");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);

        Page<StudentModel> studentPage = this.studentRepository.findByStream(givenStream, pageable);

        List<StudentModel> studentModelList = studentPage.getContent();

        // Prepare the response
        ListResponse<List<StudentDto>> listResponse = new ListResponse(
                pageNumber,
                PAGE_SIZE,
                studentPage.getTotalPages(),
                studentPage.getTotalElements(),
                this.stundentModelListToDtoList(studentModelList));

        return listResponse;
    }

    @Override
    public ListResponse<List<StudentDto>> getStudentsByCourse(String givenCourse, int pageNumber) {
        if (pageNumber == 0) {
            throw new IllegalArgumentException("Page number can't be less than 1.");
        }

        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE);

        Page<StudentModel> studentPage = this.studentRepository.findByCourse(givenCourse, pageable);

        List<StudentModel> studentModelList = studentPage.getContent();

        // Prepare the response
        ListResponse<List<StudentDto>> listResponse = new ListResponse(
                pageNumber,
                PAGE_SIZE,
                studentPage.getTotalPages(),
                studentPage.getTotalElements(),
                this.stundentModelListToDtoList(studentModelList));

        return listResponse;
    }

    @Override
    public StudentDto getStudentByEmail(String givenEmail) {
        StudentModel foundStudentModel = this.studentRepository.findByEmail(givenEmail);
        if (foundStudentModel == null) {
            throw new ResourceNotFoundException("No student exist for email: " + givenEmail);
        }

        // Convert foundStudentModel to foundStudentDto
        StudentDto foundStudentDto = this.modelMapper.map(foundStudentModel, StudentDto.class);

        // Fetch all the marksheets
        foundStudentDto.setMarksheets(this.marksheetServices.getMarksheetsForStudent(foundStudentDto.getId()));

        return foundStudentDto;
    }

    @Override
    public StudentDto getStudentById(Long givenStudentId) {
        StudentModel foundStudentModel = this.studentRepository.findById(givenStudentId).orElseThrow(
                () -> new ResourceNotFoundException("No student exist for id: " + givenStudentId));

        // Convert foundStudentModel to foundStudentDto
        StudentDto foundStudentDto = this.modelMapper.map(foundStudentModel, StudentDto.class);

        // Fetch all the marksheets
        foundStudentDto.setMarksheets(this.marksheetServices.getMarksheetsForStudent(givenStudentId));

        return foundStudentDto;
    }

    @Override
    public StudentDto getStudentByRollNumberOrRegistrationNumberOrUid(String givenSearchValue) {
        StudentModel foundStudentModel = this.studentRepository.findByRollNumberOrRegistrationNumberOrUid(givenSearchValue);

        return this.stundentModelToDto(foundStudentModel);
    }

    @Override
    public StudentDto updateStudent(StudentDto givenStudentDto, Long studentId) {
        StudentDto foundStudentDto = this.getStudentById(studentId);

        StudentModel studentModel = this.studentDtoToModel(foundStudentDto);
        studentModel.setName(givenStudentDto.getName());
        studentModel.setEmail(givenStudentDto.getEmail());
        studentModel.setPhone(givenStudentDto.getPhone());

        this.studentRepository.save(studentModel);

        return this.getStudentById(studentId);
    }

    @Override
    public Boolean deleteStudent(Long studentId) {
        StudentDto foundStudent = this.getStudentById(studentId);

        // Delete all the marksheets
        for (MarksheetDto marksheetDto : foundStudent.getMarksheets()) {
            this.marksheetServices.deleteMarksheet(marksheetDto.getId());
        }

        // Delete the student
        this.studentRepository.deleteById(studentId);

        return true;
    }

    public StudentModel studentDtoToModel(StudentDto studentDto) {
        if (studentDto == null) {
            return null;
        }

        return this.modelMapper.map(studentDto, StudentModel.class);
    }

    public List<StudentModel> studentDtoListToModelList(List<StudentDto> studentDtoList) {
        if (studentDtoList == null) {
            return new ArrayList<>();
        }

        List<StudentModel> studentModelList = new ArrayList<>();
        for (StudentDto studentDto : studentDtoList) {
            studentModelList.add(this.studentDtoToModel(studentDto));
        }

        return studentModelList;
    }

    public StudentDto stundentModelToDto(StudentModel studentModel) {
        if (studentModel == null) {
            return null;
        }

        StudentDto studentDto = this.modelMapper.map(studentModel, StudentDto.class);
        // Get all the marksheets
        studentDto.setMarksheets(this.marksheetServices.getMarksheetsForStudent(studentDto.getId()));

        return studentDto;
    }

    public List<StudentDto> stundentModelListToDtoList(List<StudentModel> stundentModelList) {
        if (stundentModelList == null) {
            return new ArrayList<>();
        }
        List<StudentDto> stundentDtoList = new ArrayList<>();

        for (StudentModel studentModel : stundentModelList) {
            stundentDtoList.add(this.stundentModelToDto(studentModel));
        }

        return stundentDtoList;
    }

    public List<StudentRow> filterStudentRowsByYear(List<StudentRow> studentRowsList, Integer year, String stream) {
        if (!stream.toUpperCase().equals(StreamType.BCOM.name())) { // For BA, BSC, BA, MA, MCOM
            // Filter the data by `year1`
            return studentRowsList.stream()
                    .filter(student -> student.getYear1() == year)
                    .collect(Collectors.toList());
        } else { // For BCOM
            // Filter the data by `year2`
            return studentRowsList.stream()
                    .filter(student -> student.getYear2() == year)
                    .collect(Collectors.toList());
        }
    }

    public List<StudentRow> filterStudentRowsByStream(List<StudentRow> studentRowsList, String stream) {
        List<StudentRow> studentList = studentRowsList.stream()
                .filter(student -> student.getStream().equalsIgnoreCase(stream))
                .collect(Collectors.toList());

        return studentList;
    }

    public List<StudentRow> filterStudentRowsBySemester(List<StudentRow> studentRowsList, Integer semester) {
        return studentRowsList.stream()
                .filter(student -> student.getSemester() == semester)
                .collect(Collectors.toList());
    }

    public List<StudentRow> filterStudentRowsByCourse(List<StudentRow> studentRowsList, String course) {
        return studentRowsList.stream()
                .filter(student -> student.getCourse() == course)
                .collect(Collectors.toList());
    }

    public List<StudentRow> filterStudentRowsByRollNumber(List<StudentRow> studentRowsList, String rollNumber) {
        return studentRowsList.stream()
                .filter(student -> student.getRoll_no().equals(rollNumber))
                .collect(Collectors.toList());
    }

}
