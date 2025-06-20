package com.data.repository.statistics;

import com.data.dto.CourseStatisticDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class StatisticRepositoryImpl implements StatisticRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CourseStatisticDTO> getStatisticByCourse() {
        String jpql = "SELECT new com.data.dto.CourseStatisticDTO(c.id, c.name, COUNT(CASE WHEN e.status = 'CONFIRM' THEN e.id ELSE NULL END), c.duration, c.image) " +
                "FROM Course c LEFT JOIN c.enrollments e " +
                "WHERE c.status = true " +
                "GROUP BY c.id, c.name, c.duration, c.image";

        return em.createQuery(jpql, CourseStatisticDTO.class)
                .getResultList();
    }

    @Override
    public List<CourseStatisticDTO> top5CoursesByEnrollmentCount() {
        String jpql = "SELECT new com.data.dto.CourseStatisticDTO(c.id, c.name, COUNT(CASE WHEN e.status = 'CONFIRM' THEN e.id ELSE NULL END), c.duration, c.image) " +
                "FROM Course c LEFT JOIN c.enrollments e " +
                "WHERE c.status = true " +
                "GROUP BY c.id, c.name, c.duration, c.image " +
                "ORDER BY COUNT(CASE WHEN e.status = 'CONFIRM' THEN e.id ELSE NULL END) DESC";

        return em.createQuery(jpql, CourseStatisticDTO.class)
                .setMaxResults(5)
                .getResultList();
    }
}