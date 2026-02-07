package com.example.VirtualBank.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String Category;
    //
    @OneToMany(mappedBy="course",cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
    private List<Student>students;
}
