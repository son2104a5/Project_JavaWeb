package com.data.repository.statistics;

import com.data.dto.CourseStatisticDTO;

import java.util.List;

public interface StatisticRepository {
    List<CourseStatisticDTO> getStatisticByCourse();
    List<CourseStatisticDTO> top5CoursesByEnrollmentCount();
}
