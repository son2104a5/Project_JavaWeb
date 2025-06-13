package com.data.repository.courses;

import com.data.dto.CourseDTO;
import com.data.entity.Course;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CourseRepositoryImpl implements CourseRepository {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Course> getCourses(int page, int size) {
        return em.createQuery("SELECT c FROM Course c", Course.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public CourseDTO getCourseById(int id) {
        Course course = em.find(Course.class, id);
        if (course != null) {
            return modelMapper.map(course, CourseDTO.class);
        }
        return null;
    }

    @Override
    public List<Course> findCoursesByName(String name, int page, int size) {
        return em.createQuery("SELECT c FROM Course c WHERE LOWER(c.name) LIKE :name", Course.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void save(CourseDTO courseDTO) {
        Course course = modelMapper.map(courseDTO, Course.class);
        em.persist(course);
    }

    @Override
    public void update(CourseDTO courseDTO) {
        Course course = em.find(Course.class, courseDTO.getId());
        if (course != null) {
            course.setName(courseDTO.getName());
            course.setInstructor(courseDTO.getInstructor());
            course.setDuration(courseDTO.getDuration());
            course.setStatus(courseDTO.getStatus());
            em.merge(course);
        }
    }

    @Override
    public void deleteById(int id) {
        Course course = em.find(Course.class, id);
        if (course != null) {
            em.remove(course);
        }
    }

    @Override
    public List<Course> filterByStatus(Boolean status) {
        return em.createQuery("SELECT c FROM Course c WHERE c.status = :status", Course.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<Course> filterByName(String name) {
        return em.createQuery("SELECT c FROM Course c WHERE c.name LIKE :name", Course.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    @Override
    public List<Course> sortByName(String name) {
        return em.createQuery("SELECT c FROM Course c ORDER BY c.name", Course.class)
                .getResultList();
    }

    @Override
    public List<Course> sortByDuration(int duration) {
        return em.createQuery("SELECT c FROM Course c ORDER BY c.duration", Course.class)
                .getResultList();
    }

    @Override
    public List<Course> sortById(int id) {
        return em.createQuery("SELECT c FROM Course c ORDER BY c.id", Course.class)
                .getResultList();
    }

    @Override
    public long countCourses() {
        return em.createQuery("SELECT COUNT(c) FROM Course c", Long.class).getSingleResult();
    }

    @Override
    public long countCoursesByName(String name) {
        return em.createQuery("SELECT COUNT(c) FROM Course c WHERE LOWER(c.name) LIKE :name", Long.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getSingleResult();
    }

    @Override
    public List<Course> getCoursesByName(String name) {
        String jpql = "FROM Course c WHERE c.name = :name";
        return em.createQuery(jpql, Course.class)
                .setParameter("name", name)
                .getResultList();
    }

}
