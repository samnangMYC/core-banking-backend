package com.trendy.cbs.entity;

import com.trendy.cbs.enums.AuthProvider;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false, length = 50)
    private AuthProvider authProvider;

    @Column(name = "auth_provider_user_id", nullable = false, updatable = false, length = 150)
    private String authProviderUserId;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role; // CUSTOMER, ADMIN, STAFF

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Actor Relationship
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Staff staff;


}
