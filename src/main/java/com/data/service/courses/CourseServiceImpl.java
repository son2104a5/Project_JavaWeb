package com.data.service.courses;

import com.data.dto.CourseDTO;
import com.data.entity.Course;
import com.data.repository.courses.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> getCourses(int page, int size) {
        return courseRepository.getCourses(page, size);
    }

    @Override
    public CourseDTO getCourseById(int id) {
        return courseRepository.getCourseById(id);
    }

    @Override
    public List<Course> findCoursesByName(String name, int page, int size) {
        return courseRepository.findCoursesByName(name, page, size);
    }

    @Override
    public void save(CourseDTO courseDTO) {
        courseRepository.save(courseDTO);
    }

    @Override
    public void update(CourseDTO courseDTO) {
        courseRepository.update(courseDTO);
    }

    @Override
    public void deleteById(int id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> filterByStatus(Boolean status) {
        return courseRepository.filterByStatus(status);
    }

    @Override
    public List<Course> filterByName(String name) {
        return courseRepository.filterByName(name);
    }

    @Override
    public List<Course> sortByName(String name) {
        return courseRepository.sortByName(name);
    }

    @Override
    public List<Course> sortByDuration(int duration) {
        return courseRepository.sortByDuration(duration);
    }

    @Override
    public List<Course> sortById(int id) {
        return courseRepository.sortById(id);
    }

    @Override
    public long countCourses() {
        return courseRepository.countCourses();
    }

    @Override
    public long countCoursesByName(String name) {
        return courseRepository.countCoursesByName(name);
    }

    @Override
    public List<Course> getCoursesByName(String name) {
        return courseRepository.getCoursesByName(name);
    }
}
