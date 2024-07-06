package com.academic360.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academic360.exceptions.ResourceNotFoundException;
import com.academic360.models.SubjectMetadataModel;
import com.academic360.repositories.SubjectMetadataRepository;
import com.academic360.services.SubjectMetadataServices;

@Service
public class SubjectMetadataServicesImpl implements SubjectMetadataServices {

    @Autowired
    private SubjectMetadataRepository subjectMetadataRepository;

    @Override
    public SubjectMetadataModel createSubjectMetadata(SubjectMetadataModel subjectMetadataModel) {
        // Check if the subject_metadata already exist
        if (this.subjectMetadataRepository.findByStreamAndSubjectTypeAndSemesterAndSubjectName(
                subjectMetadataModel.getStream(),
                subjectMetadataModel.getSubjectType(),
                subjectMetadataModel.getSemester(),
                subjectMetadataModel.getSubjectName()) != null) {
            return null;
        }

        // Return the created subject_metadata
        return this.subjectMetadataRepository.save(subjectMetadataModel);
    }

    @Override
    public List<SubjectMetadataModel> getAllSubjects() {
        return this.subjectMetadataRepository.findAll();
    }

    @Override
    public List<SubjectMetadataModel> getSubjectsByStream(String givenStream) {
        return this.subjectMetadataRepository.findByStream(givenStream);
    }

    @Override
    public List<SubjectMetadataModel> getSubjectsByCourse(String givenCourse) {
        return this.subjectMetadataRepository.findByCourse(givenCourse);
    }

    @Override
    public SubjectMetadataModel getSubjectByStreamSubjectTypeSemesterAndSubjectName(String givenStream,
            String givenSubjectType, Integer givenSemester, String givenSubjectName) {
        return this.subjectMetadataRepository.findByStreamAndSubjectTypeAndSemesterAndSubjectName(
                givenStream,
                givenSubjectType,
                givenSemester,
                givenSubjectName);
    }

    @Override
    public List<SubjectMetadataModel> getSubjectsByType(String givenSubjectType) {
        return this.subjectMetadataRepository.findBySubjectType(givenSubjectType);
    }

    @Override
    public SubjectMetadataModel getSubjectMetadataById(Long subjectMetadataId) {
        return this.subjectMetadataRepository.findById(subjectMetadataId).orElseThrow(
            () -> new ResourceNotFoundException("No subject_metadata exist for id: " + subjectMetadataId)
        );
    }

    @Override
    public SubjectMetadataModel updateSubject(SubjectMetadataModel givenSubjectMetadataModel) {
        SubjectMetadataModel foundSubjectMetadataModel = this.getSubjectMetadataById(givenSubjectMetadataModel.getId());
        // Update the fields
        foundSubjectMetadataModel.setFullMarks(givenSubjectMetadataModel.getFullMarks());
        foundSubjectMetadataModel.setCredit(givenSubjectMetadataModel.getCredit());
        foundSubjectMetadataModel.setTotalSubjects(givenSubjectMetadataModel.getTotalSubjects());
        // Return the updated subject_metadata
        return this.subjectMetadataRepository.save(foundSubjectMetadataModel);
    }

    @Override
    public Boolean deleteSubject(Long subjectMetadataId) {
        // Check if the subject_metadat exist
        SubjectMetadataModel foundSubjectMetadataModel = this.subjectMetadataRepository.findById(subjectMetadataId).orElse(null);
        if (foundSubjectMetadataModel == null) {
            return false; // Not found
        }
        // Delete the subject_metadata
        this.subjectMetadataRepository.deleteById(subjectMetadataId);

        return true; // Deleted
    }

}
