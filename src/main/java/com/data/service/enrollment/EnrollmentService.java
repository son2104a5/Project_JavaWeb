package com.data.service.enrollment;

import com.data.entity.Enrollment;
import com.data.entity.EnrollmentStatus;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    void addEnrollment(int studentId, int courseId);
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
    List<Enrollment> getEnrollmentsByStudentId(int studentId, String status, String courseName, int page, int size);
    List<Enrollment> getEnrollmentsByPage(int page, int size, String filter, String name);
    long countEnrollments(String filter, String name);
    void changeEnrollmentStatus(int enrollmentId, EnrollmentStatus status);
    long countEnrollmentsByStudentId(int studentId, String status, String courseName);
}
