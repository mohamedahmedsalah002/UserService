package com.example.VirtualBank.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Student {
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @ManyToOne(fetch=FetchType.EAGER)
    //name column in new table
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

}
