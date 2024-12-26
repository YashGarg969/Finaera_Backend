package com.yash.FinanceTracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //This will act as primaryKey and it gets auto incremented
    @Column(name = "incomeId")
    private Long id;

    private String incomeSrc;
    private Integer incomeAmount;

    private LocalDate incomeDate;
    private String incomeDesc;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false) // foreign key column
    @JsonBackReference
    private User user;

}
