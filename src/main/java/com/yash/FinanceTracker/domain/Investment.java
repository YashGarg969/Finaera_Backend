package com.yash.FinanceTracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "investments")
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long investedAmount;

    private String investmentType;

    private LocalDate investmentDate;

    private String investmentDesc;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

}
