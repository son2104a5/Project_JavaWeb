package com.data.repository;

import com.data.dto.StudentDTO;
import com.data.entity.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> findAllStudents();
    Student findStudentById(int id);
    Student findStudentByUsername(String username);
    StudentDTO save(StudentDTO studentDTO);
//    StudentDTO update(StudentDTO studentDTO);
    void deleteById(int id);
}
