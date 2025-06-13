package com.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String instructor;

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @Column(columnDefinition = "varchar(500) default null")
    private String image;

    private Boolean status = true;
}
