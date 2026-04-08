package com.myjwtai.jwtai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "screens")
public class Screen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name; // e.g., "Screen 1", "IMAX"

    @Column(nullable = false)
    private Integer totalSeats; // Overall capacity

    public Screen() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
}
