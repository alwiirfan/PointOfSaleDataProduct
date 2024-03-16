package com.pointofsale.dataSupplier.entity;

import jakarta.persistence.*;
import lombok.Data;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    protected Date createdAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    protected Date updatedAt;

}
