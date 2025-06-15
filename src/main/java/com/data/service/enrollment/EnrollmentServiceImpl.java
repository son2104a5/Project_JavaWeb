package com.data.service.enrollment;

import com.data.entity.Enrollment;
import com.data.repository.enrollment.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public void addEnrollment(int studentId, int courseId) {
        enrollmentRepository.addEnrollment(studentId, courseId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        return enrollmentRepository.getEnrollmentsByStudentId(studentId);
    }
}
