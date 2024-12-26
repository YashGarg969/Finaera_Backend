package com.yash.FinanceTracker.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@Component
@Table(name = "user_groups")
public class Group {

    @Id
    @Column(name = "group_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID groupId = UUID.randomUUID();
    private String groupName;
    private LocalDate dateCreated;
    private String groupAdminId;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinTable(
            name = "user_group_mapping",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> groupMembers = new HashSet<>();

}
