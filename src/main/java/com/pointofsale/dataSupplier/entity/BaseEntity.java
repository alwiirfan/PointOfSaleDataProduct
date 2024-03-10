package com.pointofsale.dataSupplier.entity;

import jakarta.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected String Id;

    @CreatedDate
    @Column(name = "created_at")
    protected Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected Date updatedAt;

}
