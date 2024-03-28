package com.pointofsale.dataSupplier.controller;

import com.pointofsale.dataSupplier.dto.request.NewProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductStoreRequest;
import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import com.pointofsale.dataSupplier.dto.response.PaginationResponse;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.service.ProductStoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/products-store")
@RequiredArgsConstructor
@Tag(name = "PRODUCTS STORE", description = "methods of Product Store APIs")
public class ProductStoreController {

    private final ProductStoreService productStoreService;

    @Operation(summary = "Create product store", description = "Create Product into the store")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successfully create product into the store", 
                content = @Content(schema = @Schema(implementation = CommonResponse.class))),
    })
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

    @Operation(summary = "Get all products store", description = "Get all products store")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get all products in store successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
    })
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllProductStores(@RequestParam(value = "productCode", required = false) String productCode,
                                                 @RequestParam(value = "productName", required = false) String productName,
                                                 @RequestParam(value = "category", required = false) String category,
                                                 @RequestParam(value = "merk", required = false) String merk,
                                                 @RequestParam(value = "purchasePrice", required = false) Integer purchasePrice,
                                                 @RequestParam(value = "minSellingPrice", required = false) Integer minSellingPrice,
                                                 @RequestParam(value = "maxSellingPrice", required = false) Integer maxSellingPrice,
                                                 @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
                                    ) {
        SearchProductStoreRequest request = SearchProductStoreRequest.builder()
                .productCode(productCode)
                .productName(productName)
                .Category(category)
                .merk(merk)
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
                        .page(responsePage.getNumberOfElements())
                        .size(responsePage.getSize())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get product store by id", description = "Get product store by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get product store by id successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "ProductStore not found", content = @Content)

    })
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getProductStoreById(@Parameter(description = "ID of product store to be retrieved", required = true) @PathVariable String id) {
        ProductStoreResponse productStoreResponse = productStoreService.getProductStoreById(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get product store by id successfully")
                .data(productStoreResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update product store", description = "Update existing Product store")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated product store with name successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "ProductStore not found", content = @Content)

    })
    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateProductStore(@RequestBody UpdateProductStoreRequest request,@Parameter(description = "ID of product store to be retrieved", required = true)
     @PathVariable String id) {
        ProductStoreResponse productStoreResponse = productStoreService.updateProductStore(request, id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Updated product store with name successfully")
                .data(productStoreResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete product store", description = "Delete existing Product store")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delete product store successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "ProductStore not found", content = @Content)

    })
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteProductStore(@Parameter(description = "ID of product store to be retrieved", required = true) @PathVariable String id) {
        productStoreService.deleteProductStore(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete product store successfully")
                .build();

        return ResponseEntity.ok(response);
    }

}
