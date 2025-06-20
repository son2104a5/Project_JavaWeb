package com.data.entity;

import com.data.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean sex;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @Column(nullable = false)
    private Boolean role = false;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @Column(name = "image", columnDefinition = "varchar(500) default null")
    private String image;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
