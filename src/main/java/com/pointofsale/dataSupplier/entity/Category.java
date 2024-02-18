package com.pointofsale.dataSupplier.entity;

import com.pointofsale.dataSupplier.constant.ECategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category_product")
@Builder(toBuilder = true)
public class Category extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private ECategory eCategory;
}
