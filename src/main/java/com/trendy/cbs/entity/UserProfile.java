package com.trendy.cbs.entity;

import com.trendy.cbs.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private Integer phoneNumber;
    private String occupation;
    private String nationality;
    private String maritalStatus;
    private String profileImage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",unique = true)
    private User user;

}
