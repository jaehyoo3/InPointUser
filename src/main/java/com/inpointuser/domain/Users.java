package com.inpointuser.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table
@NoArgsConstructor
@Getter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long profileViewCount = 0L;

    private Long point = 0L;

    @CreationTimestamp
    private Instant createdAt;

    public void viewCountPlus() {
        this.profileViewCount++;
    }

    public void chargePoint(Long amount) {
        if (amount != null && amount > 0) {
            this.point += amount;
        }
    }
}
