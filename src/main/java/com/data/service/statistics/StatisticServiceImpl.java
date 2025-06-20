package com.data.service.statistics;

import com.data.dto.CourseStatisticDTO;
import com.data.entity.Course;
import com.data.repository.statistics.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StatisticRepository statisticRepository;

    @Override
    public List<CourseStatisticDTO> getStatisticByCourse() {
        return statisticRepository.getStatisticByCourse();
    }

    @Override
    public List<CourseStatisticDTO> top5CoursesByEnrollmentCount() {
        return statisticRepository.top5CoursesByEnrollmentCount();
    }
}
