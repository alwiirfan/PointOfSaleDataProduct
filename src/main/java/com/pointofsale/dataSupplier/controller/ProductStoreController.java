package com.pointofsale.dataSupplier.controller;

import com.pointofsale.dataSupplier.dto.request.NewProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductStoreRequest;
import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import com.pointofsale.dataSupplier.dto.response.PaginationResponse;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.service.ProductStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping(path = "/api/v1/product-store")
@RequiredArgsConstructor
public class ProductStoreController {

    private final ProductStoreService productStoreService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewProductStore(@RequestBody NewProductStoreRequest request) {
        ProductStoreResponse productStoreResponse = productStoreService.createProductStore(request);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create product into the store")
                .data(productStoreResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllProductStore(@RequestParam(value = "productCode", required = false) String productCode,
                                    @RequestParam(value = "productName", required = false) String productName,
                                    @RequestParam(value = "purchasePrice", required = false) Integer purchasePrice,
                                    @RequestParam(value = "minSellingPrice", required = false) Integer minSellingPrice,
                                    @RequestParam(value = "maxSellingPrice", required = false) Integer maxSellingPrice,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
                                    ) {
        SearchProductStoreRequest request = SearchProductStoreRequest.builder()
                .productCode(productCode)
                .productName(productName)
                .purchasePrice(purchasePrice)
                .minSellingPrice(minSellingPrice)
                .maxSellingPrice(maxSellingPrice)
                .build();

        Page<ProductStoreResponse> responsePage = productStoreService.getAllProductStore(request, page, size);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all products in store successfully")
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
    public ResponseEntity<?> getProductStoreById(@PathVariable String id) {
        ProductStoreResponse productStoreResponse = productStoreService.getProductStoreById(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get product store by id successfully")
                .data(productStoreResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateProductStore(@RequestBody UpdateProductStoreRequest request, @PathVariable String id) {
        ProductStoreResponse productStoreResponse = productStoreService.updateProductStore(request, id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Updated product store with name " + productStoreResponse.getProductName() + " Successfully")
                .data(productStoreResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteProductStore(@PathVariable String id) {
        productStoreService.deleteProductStore(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete product store successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}
