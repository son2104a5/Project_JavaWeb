package com.data.repository.students;

import com.data.dto.StudentDTO;
import com.data.entity.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> findAllStudents();
    Student findStudentById(int id);
    StudentDTO findStudentByEmail(String email);
    StudentDTO save(StudentDTO studentDTO);
    void update(StudentDTO studentDTO);
    List<Student> getStudentsByPage(int page, int size, String sort, String name);
    long countTotalStudents();
    void lockAccount(int id);
    StudentDTO findStudentByPhone(String phone);
}
