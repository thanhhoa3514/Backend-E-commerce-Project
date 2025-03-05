package com.project.e_commerce.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "provider",nullable = false, length = 100)
    private String provider;

    @Column(name = "provider_id")
    private Long providerId;
}
