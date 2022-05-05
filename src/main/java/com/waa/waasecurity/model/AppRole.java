package com.waa.waasecurity.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AppRole {
    @Id
    @GeneratedValue
    private Long id;
    private String role;
}