package com.data.service.students;

import com.data.dto.StudentDTO;
import com.data.entity.Student;
import com.data.repository.students.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAllStudents();
    }

    @Override
    public Student findStudentById(int id) {
        return studentRepository.findStudentById(id);
    }

    @Override
    public Student findStudentByUsernameOrEmail(String username, String email) {
        return studentRepository.findStudentByUsernameOrEmail(username, email);
    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        return studentRepository.save(studentDTO);
    }

    @Override
    public void deleteById(int id) {
        studentRepository.deleteById(id);
    }
}
