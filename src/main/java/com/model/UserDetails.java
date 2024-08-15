package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private User user;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "secondary_email", nullable = true)
    private String secondaryEmail;
    @Column(name = "linkedin_profile", nullable = true)
    private String linkedInProfile;
}
