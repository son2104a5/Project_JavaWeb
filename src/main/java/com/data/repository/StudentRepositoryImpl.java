package com.data.repository;

import com.data.dto.StudentDTO;
import com.data.entity.Student;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class StudentRepositoryImpl implements StudentRepository {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Student> findAllStudents() {
        return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    @Override
    public Student findStudentById(int id) {
        return em.find(Student.class, id);
    }

    @Override
    public Student findStudentByUsername(String username) {
        try {
            return em.createQuery("SELECT s FROM Student s WHERE s.username = :username", Student.class)
                     .setParameter("username", username)
                     .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        Student student = modelMapper.map(studentDTO, Student.class);
        em.persist(student);
        return modelMapper.map(student, StudentDTO.class);
    }

//    @Override
//    public StudentDTO update(StudentDTO studentDTO) {
//        Student student;
//        student = em.find(Student.class, studentDTO.getId());
//        if (student != null) {
//            student.setUsername(studentDTO.getUsername());
//            student.setName(studentDTO.getName());
//            student.setDob(studentDTO.getDob());
//            student.setEmail(studentDTO.getEmail());
//            student.setSex(studentDTO.getSex());
//            student.setPhone(studentDTO.getPhone());
//            student.setPassword(studentDTO.getPassword());
//            student.setRole(studentDTO.getRole());
//            em.merge(student);
//        }
//        return modelMapper.map(student, StudentDTO.class);
//    }

    @Override
    public void deleteById(int id) {

    }
}
