package com.pointofsale.dataSupplier.entity;

import com.pointofsale.dataSupplier.constant.ERole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Builder(toBuilder = true)
public class Role extends BaseEntity {
    
    @Enumerated(EnumType.STRING)
    private ERole role;

}
