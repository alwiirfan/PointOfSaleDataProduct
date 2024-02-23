package com.pointofsale.dataSupplier.controller;

import com.pointofsale.dataSupplier.dto.request.NewProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import com.pointofsale.dataSupplier.dto.response.PaginationResponse;
import com.pointofsale.dataSupplier.dto.response.ProductSupplierResponse;
import com.pointofsale.dataSupplier.service.ProductSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products-supplier")
public class ProductSupplierController {

    private final ProductSupplierService productSupplierService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createProductSupplier(@RequestBody NewProductSupplierRequest request) {
        ProductSupplierResponse productSupplierResponse = productSupplierService.create(request);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("created product supplier successfully")
                .data(productSupplierResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllProductSuppliers(@RequestParam(value = "productName", required = false) String productName,
                                                    @RequestParam(value = "minUnitPrice", required = false) Integer minUnitPrice,
                                                    @RequestParam(value = "maxUnitPrice", required = false) Integer maxUnitPrice,
                                                    @RequestParam(value = "totalItem", required = false) Integer totalItem,
                                                    @RequestParam(value = "minTotalPrice", required = false) Integer minTotalPrice,
                                                    @RequestParam(value = "maxTotalPrice", required = false) Integer maxTotalPrice,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchProductSupplierRequest request = SearchProductSupplierRequest.builder()
                .productName(productName)
                .minTotalPrice(minTotalPrice)
                .maxTotalPrice(maxTotalPrice)
                .totalItem(totalItem)
                .minUnitPrice(minUnitPrice)
                .maxUnitPrice(maxUnitPrice)
                .build();

        Page<ProductSupplierResponse> responsePage = productSupplierService.getAll(request, page, size);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get All data products supplier successfully")
                .data(responsePage.getContent())
                .pagination(PaginationResponse.builder()
                        .count(responsePage.getTotalElements())
                        .totalPage(responsePage.getTotalPages())
                        .page(page)
                        .size(size)
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getProductSupplierById(@PathVariable String id) {
        ProductSupplierResponse productSupplierResponse = productSupplierService.getById(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get product supplier by id successfully")
                .data(productSupplierResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateProductSupplier(@RequestBody UpdateProductSupplierRequest request, @PathVariable String id) {
        ProductSupplierResponse productSupplierResponse = productSupplierService.update(request, id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update data product supplier successfully")
                .data(productSupplierResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteProductSupplier(@PathVariable String id) {
        productSupplierService.delete(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete data product supplier successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}