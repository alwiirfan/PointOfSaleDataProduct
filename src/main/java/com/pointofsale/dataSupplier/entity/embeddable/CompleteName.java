package com.pointofsale.dataSupplier.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Embeddable
public class CompleteName {
    
    @Column(name = "full_name", length = 300, nullable = false)
    private String fullName;

    @Column(name = "call_name", length = 100, nullable = false)
    private String callName;
}
