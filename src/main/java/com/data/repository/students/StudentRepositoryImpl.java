package com.data.repository.students;

import com.data.dto.StudentDTO;
import com.data.entity.Student;
import lombok.var;
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
        return em.createQuery("SELECT s FROM Student s WHERE s.role = false", Student.class).getResultList();
    }

    @Override
    public Student findStudentById(int id) {
        return em.find(Student.class, id);
    }

    @Override
    public StudentDTO findStudentByEmail(String email) {
        String query = "SELECT s FROM Student s WHERE s.email = :email";
        var jpql = em.createQuery(query, Student.class);
        jpql.setParameter("email", email);

        List<Student> students = jpql.getResultList();
        if (!students.isEmpty()) {
            return modelMapper.map(students.get(0), StudentDTO.class);
        }
        return null;
    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        Student student = modelMapper.map(studentDTO, Student.class);
        student.setImage("https://i.pinimg.com/736x/8f/1c/a2/8f1ca2029e2efceebd22fa05cca423d7.jpg");
        em.persist(student);
        return modelMapper.map(student, StudentDTO.class);
    }

    @Override
    public void update(StudentDTO studentDTO) {
        Student student = em.find(Student.class, studentDTO.getId());
        if (student != null) {
            String oldPassword = student.getPassword();

            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(studentDTO, student);

            if (studentDTO.getPassword() == null || studentDTO.getPassword().isEmpty()) {
                student.setPassword(oldPassword);
            } else {
                student.setPassword(studentDTO.getPassword());
            }

            em.merge(student);
        }
    }


    @Override
    public List<Student> getStudentsByPage(int page, int size, String sort, String name) {
        String query = "SELECT s FROM Student s WHERE s.role = false";
        if (name != null && !name.isEmpty()) {
            query += " AND s.name LIKE :name";
        }
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "id_asc":
                    query += " ORDER BY s.id ASC";
                    break;
                case "id_desc":
                    query += " ORDER BY s.id DESC";
                    break;
                case "name_asc":
                    query += " ORDER BY s.name ASC";
                    break;
                case "name_desc":
                    query += " ORDER BY s.name DESC";
                    break;
            }
        }
        var jpql = em.createQuery(query, Student.class);
        if (name != null && !name.isEmpty()) {
            jpql.setParameter("name", "%" + name.toLowerCase() + "%");
        }
        return jpql.setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long countTotalStudents() {
        return em.createQuery("SELECT COUNT(s.id) FROM Student s WHERE s.role = false ", Long.class)
                .getSingleResult();
    }

    @Override
    public void lockAccount(int id) {
        Student student = em.find(Student.class, id);
        if (student != null) {
            student.setStatus(!student.getStatus());
            em.merge(student);
        }
    }

    @Override
    public StudentDTO findStudentByPhone(String phone) {
        String query = "SELECT s FROM Student s WHERE s.phone = :phone";
        var jpql = em.createQuery(query, Student.class);
        jpql.setParameter("phone", phone);

        List<Student> students = jpql.getResultList();
        if (!students.isEmpty()) {
            return modelMapper.map(students.get(0), StudentDTO.class);
        }
        return null;
    }
}
