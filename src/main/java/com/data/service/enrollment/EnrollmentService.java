package com.data.service.enrollment;

import com.data.entity.Enrollment;

import java.util.List;

public interface EnrollmentService {
    void addEnrollment(int studentId, int courseId);
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
}
