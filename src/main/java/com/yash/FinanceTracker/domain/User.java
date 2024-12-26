package com.yash.FinanceTracker.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yash.FinanceTracker.constants.UserRoles;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Component
@Table(name = "user")
public class User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "userId")

    @Id
    private String userId;

    //private Long id;

    private String userName;

    private String userEmail;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)  // Store enum as String in the database
    @Column(name = "role")
    private Set<UserRoles> userRolesSet= new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Expense> expenses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Income> incomes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Investment> investments;

    @ManyToMany(mappedBy = "groupMembers",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Group> groups= new HashSet<>();
}
