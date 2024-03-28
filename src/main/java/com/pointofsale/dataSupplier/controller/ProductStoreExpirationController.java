    package com.pointofsale.dataSupplier.controller;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.pointofsale.dataSupplier.dto.request.NewProductStoreExpirationRequest;
    import com.pointofsale.dataSupplier.dto.response.CommonResponse;
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
    }
