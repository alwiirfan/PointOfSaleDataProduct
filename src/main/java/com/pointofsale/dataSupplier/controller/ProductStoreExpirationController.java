    package com.pointofsale.dataSupplier.controller;

    import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

    import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.request.SearchProductStoreExpirationRequest;
import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import com.pointofsale.dataSupplier.dto.response.PaginationResponse;
import com.pointofsale.dataSupplier.dto.response.ProductStoreExpirationResponse;
    import com.pointofsale.dataSupplier.service.ProductStoreExpirationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
    import lombok.RequiredArgsConstructor;

    @RestController
    @RequestMapping("/api/v1/products-store-expiration")
    @RequiredArgsConstructor
    @Tag(name = "PRODUCT STORE EXPIRATION", description = "methods of Product Store Expiration APIs")
    public class ProductStoreExpirationController {
        
        private final ProductStoreExpirationService productStoreExpirationService;

        @Operation(summary = "Create product store expiration", description = "Create Product Store Expiration")
        @ApiResponses({
            @ApiResponse(responseCode = "201", description = "created product store expiration successfully", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class))),
        })
        @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
        )
        public ResponseEntity<?> createNewProductStoreExpiration(@RequestBody NewProductStoreExpirationRequest request) {
            ProductStoreExpirationResponse productStoreExpirationResponse = productStoreExpirationService.create(request);

            CommonResponse<?> response = CommonResponse.builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Successfully create product store expiration")
                    .data(productStoreExpirationResponse)
                    .build();

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(response);
        }

        @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
        )
        public ResponseEntity<?> getById(@PathVariable("id") String id) {
            ProductStoreExpirationResponse productStoreExpirationResponse = productStoreExpirationService.getById(id);

            CommonResponse<?> response = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully get product store expiration")
                    .data(productStoreExpirationResponse)
                    .build();

            return ResponseEntity.ok(response);
        }

        @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
        )
        public ResponseEntity<?> getAll(@RequestParam(name = "category", required = false) String category,
                                        @RequestParam(name = "startDate", required = false) String startDate,
                                        @RequestParam(name = "endDate", required = false) String endDate, 
                                        @RequestParam(name = "productStoreExpirationDetailName", required = false) String productStoreExpirationDetailName, 
                                        @RequestParam(name = "productStoreExpirationDetailCode", required = false) String productStoreExpirationDetailCode, 
                                        @RequestParam(name = "productExpirationDetailMinTotalItem", required = false) Integer productExpirationDetailMinTotalItem, 
                                        @RequestParam(name = "productExpirationDetailMaxTotalItem", required = false) Integer productExpirationDetailMaxTotalItem, 
                                        @RequestParam(name = "category", required = false, defaultValue = "0") Integer page, 
                                        @RequestParam(name = "category", required = false, defaultValue = "10") Integer size) {
            
            SearchProductStoreExpirationRequest request = SearchProductStoreExpirationRequest.builder()
                    .category(category)
                    .startDate(startDate)
                    .endDate(endDate)
                    .productExpirationDetailCode(productStoreExpirationDetailCode)
                    .productExpirationDetailName(productStoreExpirationDetailName)
                    .productExpirationDetailMinTotalItem(productExpirationDetailMinTotalItem)
                    .productExpirationDetailMaxTotalItem(productExpirationDetailMaxTotalItem)
                    .build();

            Page<ProductStoreExpirationResponse> responsePage = productStoreExpirationService.getAll(request, page, size);
            
            CommonResponse<?> response = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully get all product store expiration")
                    .data(responsePage.getContent())
                    .pagination(PaginationResponse.builder()
                        .count(responsePage.getTotalElements())
                        .totalPage(responsePage.getTotalPages())
                        .page(responsePage.getNumberOfElements())
                        .size(responsePage.getSize())
                        .build())
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
    }
