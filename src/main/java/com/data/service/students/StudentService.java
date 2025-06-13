package com.data.service.students;

import com.data.dto.StudentDTO;
import com.data.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAllStudents();
    Student findStudentById(int id);
    Student findStudentByUsernameOrEmail(String username, String email);
    StudentDTO save(StudentDTO studentDTO);
    //    StudentDTO update(StudentDTO studentDTO);
    void deleteById(int id);
}
