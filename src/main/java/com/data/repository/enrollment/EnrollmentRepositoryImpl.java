package com.data.repository.enrollment;

import com.data.entity.Enrollment;
import com.data.entity.EnrollmentStatus;
import lombok.var;
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
    public List<Enrollment> getAllEnrollments() {
        String jpql = "SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.student";
        return em.createQuery(jpql, Enrollment.class).getResultList();
    }

    @Override
    public void addEnrollment(int studentId, int courseId) {
        String sql = "INSERT INTO enrollment (student_id, course_id, registered_at, status) VALUES (:studentId, :courseId, CURRENT_TIMESTAMP, 'WAITING')";
        em.createNativeQuery(sql)
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        String jpql = "SELECT e FROM Enrollment e JOIN FETCH e.course WHERE e.student.id = :studentId";
        return em.createQuery(jpql, Enrollment.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId, String status, String courseName, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT e FROM Enrollment e JOIN FETCH e.course WHERE e.student.id = :studentId");
        if (status != null && !status.isEmpty()) {
            jpql.append(" AND e.status = :status");
        }
        if (courseName != null && !courseName.isEmpty()) {
            jpql.append(" AND LOWER(e.course.name) LIKE :courseName");
        }
        var query = em.createQuery(jpql.toString(), Enrollment.class)
                .setParameter("studentId", studentId)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size);
        if (status != null && !status.isEmpty()) {
            try {
                EnrollmentStatus enumStatus = EnrollmentStatus.valueOf(status);
                query.setParameter("status", enumStatus);
            } catch (IllegalArgumentException e) {
                e.fillInStackTrace();
            }
        }
        if (courseName != null && !courseName.isEmpty()) {
            query.setParameter("courseName", "%" + courseName.toLowerCase() + "%");
        }
        return query.getResultList();
    }

    @Override
    public List<Enrollment> getEnrollmentsByPage(int page, int size, String filter, String name) {
        StringBuilder jpql = new StringBuilder("SELECT e FROM Enrollment e JOIN FETCH e.course JOIN FETCH e.student WHERE 1=1");

        if (filter != null && !filter.isEmpty()) {
            jpql.append(" AND e.status = :filter");
        }
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(e.course.name) LIKE :name");
        }

        var query = em.createQuery(jpql.toString(), Enrollment.class)
                .setFirstResult(page * size)
                .setMaxResults(size);

        if (filter != null && !filter.isEmpty()) {
            try {
                EnrollmentStatus enumFilter = EnrollmentStatus.valueOf(filter);
                query.setParameter("filter", enumFilter);
            } catch (IllegalArgumentException e) {
                e.fillInStackTrace();
            }
        }
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        }

        return query.getResultList();
    }

    public long countEnrollments(String filter, String name) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(e) FROM Enrollment e JOIN e.course WHERE 1=1");
        if (filter != null && !filter.isEmpty()) {
            jpql.append(" AND e.status = :filter");
        }
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND LOWER(e.course.name) LIKE :name");
        }

        var query = em.createQuery(jpql.toString(), Long.class);
        if (filter != null && !filter.isEmpty()) {
            try {
                EnrollmentStatus enumFilter = EnrollmentStatus.valueOf(filter);
                query.setParameter("filter", enumFilter);
            } catch (IllegalArgumentException e) {
                return 0; // Hoặc xử lý khác
            }
        }
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        }
        return (long) query.getSingleResult();
    }

    @Override
    public void changeEnrollmentStatus(int enrollmentId, EnrollmentStatus status) {
        String jpql = "UPDATE Enrollment e SET e.status = :status WHERE e.id = :enrollmentId";
        try {
            em.createQuery(jpql)
                    .setParameter("status", status)
                    .setParameter("enrollmentId", enrollmentId)
                    .executeUpdate();
        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public long countEnrollmentsByStudentId(int studentId, String status, String courseName) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId");
        if (status != null && !status.isEmpty()) {
            jpql.append(" AND e.status = :status");
        }
        if (courseName != null && !courseName.isEmpty()) {
            jpql.append(" AND LOWER(e.course.name) LIKE :courseName");
        }

        var query = em.createQuery(jpql.toString(), Long.class)
                .setParameter("studentId", studentId);

        if (status != null && !status.isEmpty()) {
            try {
                EnrollmentStatus enumStatus = EnrollmentStatus.valueOf(status);
                query.setParameter("status", enumStatus);
            } catch (IllegalArgumentException e) {
                return 0;
            }
        }
        if (courseName != null && !courseName.isEmpty()) {
            query.setParameter("courseName", "%" + courseName.toLowerCase() + "%");
        }

        return query.getSingleResult();
    }
}
