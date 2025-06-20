package com.data.service.statistics;

import com.data.dto.CourseStatisticDTO;

import java.util.List;

public interface StatisticService {
    List<CourseStatisticDTO> getStatisticByCourse();
    List<CourseStatisticDTO> top5CoursesByEnrollmentCount();
}
