package com.data.repository.enrollment;

import com.data.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository {
    void addEnrollment(int studentId, int courseId);
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
}
