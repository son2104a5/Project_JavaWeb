package com.data.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class CourseDTO {
    private int id;

    @NotBlank(message = "Tên khóa học không được để trống")
    private String name;

    @Min(value = 1, message = "Thời gian khóa học phải lớn hơn hoặc bằng 1")
    private int duration;

    @NotBlank(message = "Tên giảng viên không được để trống")
    private String instructor;

    private LocalDate createdAt = LocalDate.now();

    private MultipartFile image;

    private Boolean status = true;

    private String imageUrl;
}
