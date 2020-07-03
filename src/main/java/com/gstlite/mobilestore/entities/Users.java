package com.gstlite.mobilestore.entities;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "full_name", nullable = false)
    private String fullname;

    @Getter
    @Setter
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "is_disabled", nullable = false)
    private boolean isDisabled;
}
