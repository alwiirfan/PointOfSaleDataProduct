package com.pointofsale.dataSupplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaginationResponse {
    private Long count;
    private Integer page;
    private Integer totalPage;
    private Integer size;
}
