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
    public StudentDTO findStudentByEmail(String email) {
        return studentRepository.findStudentByEmail(email);
    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        return studentRepository.save(studentDTO);
    }

    @Override
    public void update(StudentDTO studentDTO) {
        studentRepository.update(studentDTO);
    }

    @Override
    public List<Student> getStudentsByPage(int page, int size, String sort, String name) {
        return studentRepository.getStudentsByPage(page, size, sort, name);
    }

    @Override
    public long countTotalStudents() {
        return studentRepository.countTotalStudents();
    }

    @Override
    public void lockAccount(int id) {
        studentRepository.lockAccount(id);
    }

    @Override
    public StudentDTO findStudentByPhone(String phone) {
        return studentRepository.findStudentByPhone(phone);
    }
}
