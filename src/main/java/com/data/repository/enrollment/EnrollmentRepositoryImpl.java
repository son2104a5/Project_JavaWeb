package com.data.repository.enrollment;

import com.data.entity.Enrollment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class EnrollmentRepositoryImpl implements EnrollmentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void addEnrollment(int studentId, int courseId) {
        String jpql = "INSERT INTO Enrollment (student_id, course_id, registered_at, status) VALUES (:studentId, :courseId, CURRENT_TIMESTAMP, 'WAITING')";
        em.createNativeQuery(jpql)
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        String jpql = "SELECT e FROM Enrollment e WHERE e.student.id = :studentId";
        return em.createQuery(jpql, Enrollment.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }
}
