package com.data.repository.courses;

import com.data.dto.CourseDTO;
import com.data.entity.Course;

import java.util.List;

public interface CourseRepository {
    List<Course> getCourses(int page, int size);
    CourseDTO getCourseById(int id);
    List<Course> findCoursesByName(String name, int page, int size);
    List<Course> getCoursesByName(String name);
    void save(CourseDTO courseDTO);
    void update(CourseDTO courseDTO);
    void deleteById(int id);
    List<Course> filterByStatus(Boolean status);
    List<Course> filterByName(String name);
    List<Course> sortByName(String name);
    List<Course> sortByDuration(int duration);
    List<Course> sortById(int id);
    long countCourses();
    long countCoursesByName(String name);
}
