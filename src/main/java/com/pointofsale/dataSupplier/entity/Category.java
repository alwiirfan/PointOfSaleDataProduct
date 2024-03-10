package com.pointofsale.dataSupplier.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
@Builder(toBuilder = true)
public class Category extends BaseEntity {
    @Column(name = "category_type", length = 150, nullable = false, unique = true)
    private String category;
}
