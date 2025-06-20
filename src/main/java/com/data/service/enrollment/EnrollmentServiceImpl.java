package com.data.service.enrollment;

import com.data.entity.Enrollment;
import com.data.entity.EnrollmentStatus;
import com.data.repository.enrollment.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.getAllEnrollments();
    }

    @Override
    public void addEnrollment(int studentId, int courseId) {
        enrollmentRepository.addEnrollment(studentId, courseId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        return enrollmentRepository.getEnrollmentsByStudentId(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId, String status, String courseName, int page, int size) {
        return enrollmentRepository.getEnrollmentsByStudentId(studentId, status, courseName, page, size);
    }

    @Override
    public List<Enrollment> getEnrollmentsByPage(int page, int size, String filter, String name) {
        return enrollmentRepository.getEnrollmentsByPage(page, size, filter, name);
    }

    @Override
    public long countEnrollments(String filter, String name) {
        return enrollmentRepository.countEnrollments(filter, name);
    }

    @Override
    public void changeEnrollmentStatus(int enrollmentId, EnrollmentStatus status) {
        enrollmentRepository.changeEnrollmentStatus(enrollmentId, status);
    }

    @Override
    public long countEnrollmentsByStudentId(int studentId, String status, String courseName) {
        return enrollmentRepository.countEnrollmentsByStudentId(studentId, status, courseName);
    }
}
