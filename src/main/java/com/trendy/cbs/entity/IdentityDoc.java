package com.trendy.cbs.entity;

import com.trendy.cbs.enums.DocStatus;
import com.trendy.cbs.enums.IdentityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity(name = "identity_doc")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentityDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    private String number;

    private LocalDateTime expDate;

    private LocalDateTime verifiedDate;

    private String docImage;

    @Enumerated(EnumType.STRING)
    private DocStatus docStatus;

    @Enumerated(EnumType.STRING)
    private IdentityType identityType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",unique = true)
    private User user;

}