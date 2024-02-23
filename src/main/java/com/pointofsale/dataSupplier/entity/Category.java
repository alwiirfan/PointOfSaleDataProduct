package com.pointofsale.dataSupplier.entity;

import com.pointofsale.dataSupplier.constant.ECategory;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private ECategory category;
}
