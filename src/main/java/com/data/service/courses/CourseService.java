package com.data.service.courses;

import com.data.dto.CourseDTO;
import com.data.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getCourses(int page, int size, String name, String status, String sort);
    List<Course> findCoursesByName(String name, int page, int size);
    CourseDTO getCourseById(int id);
    void save(CourseDTO courseDTO);
    void update(CourseDTO courseDTO);
    void deleteById(int id);
    List<Course> getCoursesByName(String name);
    List<Course> filterByStatus(Boolean status);
    List<Course> filterByName(String name);
    List<Course> sortByName(String name);
    List<Course> sortByDuration(int duration);
    List<Course> sortById(int id);
    long countCourses(String name, String status);
    long countCoursesByName(String name);
}