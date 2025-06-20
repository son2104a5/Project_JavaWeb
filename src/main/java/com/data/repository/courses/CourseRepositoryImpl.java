package com.data.repository.courses;

import com.data.dto.CourseDTO;
import com.data.entity.Course;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    public List<Course> getCourses(int page, int size, String name, String status, String sort) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Course c WHERE 1=1");
        if (name != null && !name.trim().isEmpty()) {
            jpql.append(" AND LOWER(c.name) LIKE :name");
        }
        if (status != null) {
            jpql.append(" AND c.status = :status");
        } else {
            jpql.append(" AND c.status = true");
        }
        if (sort != null) {
            switch (sort) {
                case "id_asc":
                    jpql.append(" ORDER BY c.id ASC");
                    break;
                case "id_desc":
                    jpql.append(" ORDER BY c.id DESC");
                    break;
                case "name_asc":
                    jpql.append(" ORDER BY c.name ASC");
                    break;
                case "name_desc":
                    jpql.append(" ORDER BY c.name DESC");
                    break;
            }
        }

        TypedQuery<Course> query = em.createQuery(jpql.toString(), Course.class);
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        }
        if (status != null) {
            query.setParameter("status", Boolean.parseBoolean(status));
        }
        return query.setFirstResult(page) // Sửa ở đây
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Course> findCoursesByName(String name, int page, int size) {
        return getCourses(page, size, name, null, null);
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
            course.setImage(courseDTO.getImage());
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
    public long countCourses(String name, String status) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(c) FROM Course c WHERE 1=1");
        if (name != null && !name.trim().isEmpty()) {
            jpql.append(" AND LOWER(c.name) LIKE :name");
        }
        if (status != null) {
            jpql.append(" AND c.status = :status");
        } else {
            jpql.append(" AND c.status = true");
        }

        TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
        if (name != null && !name.trim().isEmpty()) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
        }
        if (status != null) {
            query.setParameter("status", Boolean.parseBoolean(status));
        }
        return query.getSingleResult();
    }

    @Override
    public long countCoursesByName(String name) {
        return countCourses(name, null);
    }

    @Override
    public List<Course> getCourseByStudentId(int studentId) {
        return em.createQuery("SELECT c FROM Course c JOIN enrollment e ON c.id = e.course_id WHERE e.student.id = :studentId", Course.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    @Override
    public List<Course> getCoursesByName(String name) {
        return em.createQuery("SELECT c FROM Course c WHERE LOWER(c.name) LIKE :name AND c.status = true", Course.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getResultList();
    }
}