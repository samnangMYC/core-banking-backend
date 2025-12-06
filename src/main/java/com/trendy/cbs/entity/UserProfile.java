package com.trendy.cbs.entity;

import com.trendy.cbs.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity representing a user's profile details.
 *
 * <p>This entity has a one-to-one bidirectional relationship with {@link User},
 * allowing cascading persistence and lazy loading for efficiency.</p>
 *
 * @see User
 * @see Gender
 * @Entity
 * @Table(name = "user_profile")
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profile")
@Builder
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = true)
    private User user;

}
