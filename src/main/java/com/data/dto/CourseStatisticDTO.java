package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatisticDTO {
    private int id;
    private String name;
    private Long totalStudent;
    private int duration;
    private String image;
}