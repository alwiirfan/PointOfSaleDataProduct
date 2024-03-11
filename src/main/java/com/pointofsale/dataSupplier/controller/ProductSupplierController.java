package com.pointofsale.dataSupplier.controller;

import com.pointofsale.dataSupplier.dto.request.NewProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateProductSupplierRequest;
import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import com.pointofsale.dataSupplier.dto.response.PaginationResponse;
import com.pointofsale.dataSupplier.dto.response.ProductSupplierResponse;
import com.pointofsale.dataSupplier.service.ProductSupplierService;

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
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products-supplier")
@Tag(name = "PRODUCTS SUPPLIER", description = "methods of Product Supplier APIs")
public class ProductSupplierController {

    private final ProductSupplierService productSupplierService;

    @Operation(summary = "Create product supplier", description = "Create Product into the supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "created product supplier successfully", 
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class))),
    })
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

    @Operation(summary = "Get all products supplier", description = "Get all products supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get All data products supplier successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
    })
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

    @Operation(summary = "Get product supplier by id", description = "Get product supplier by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get product supplier by id successfully" , 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "ProductSupplier not found", content = @Content)

    })
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getProductSupplierById(@Parameter(description = "ID of product supplier to be retrieved", required = true) 
        @PathVariable String id) {
        ProductSupplierResponse productSupplierResponse = productSupplierService.getById(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get product supplier by id successfully")
                .data(productSupplierResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update product supplier", description = "Update existing Product supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update data product supplier successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = CommonResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "ProductSupplier not found", content = @Content)

    })
    @PutMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateProductSupplier(@RequestBody UpdateProductSupplierRequest request, 
        @Parameter(description = "ID of product supplier to be retrieved", required = true)
        @PathVariable String id) {
        ProductSupplierResponse productSupplierResponse = productSupplierService.update(request, id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Update data product supplier successfully")
                .data(productSupplierResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete product supplier", description = "Delete existing Product supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delete data product supplier successfully" , 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
        @ApiResponse(responseCode = "404", description = "ProductSupplier not found", content = @Content)

    })
    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteProductSupplier(@Parameter(description = "ID of product supplier to be retrieved", required = true) 
        @PathVariable String id) {
        productSupplierService.delete(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete data product supplier successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary ="Get all product supplier by category", description = "GET methods of Product Supplier APIs")
    @GetMapping(
            path = "/category",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllProductSupplierByCategory(@Parameter(description = "Category of product supplier to be retrieved", required = true) 
                                                                @RequestParam(value = "category") String category,
                                                             @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                             @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Page<ProductSupplierResponse> responsePage = productSupplierService.getAllProductSupplierByCategory(category, page, size);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get All data products supplier successfully")
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
}