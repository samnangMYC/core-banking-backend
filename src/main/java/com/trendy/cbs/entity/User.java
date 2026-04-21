package com.trendy.cbs.entity;

import com.trendy.cbs.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_user_id", unique = true, nullable = false)
    private String keycloakUserId;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role; // CUSTOMER, ADMIN, STAFF

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Relationship to Customer
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;

}
