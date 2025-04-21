package com.project.e_commerce.models.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Data

public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Other fields

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    // Rest of the class
}