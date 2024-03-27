package com.pointofsale.dataSupplier.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pointofsale.dataSupplier.dto.request.NewTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.SearchSalesHistoryRequest;
import com.pointofsale.dataSupplier.dto.request.SearchTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateTransactionRequest;
import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import com.pointofsale.dataSupplier.dto.response.PaginationResponse;
import com.pointofsale.dataSupplier.dto.response.TotalSales;
import com.pointofsale.dataSupplier.dto.response.TransactionResponse;
import com.pointofsale.dataSupplier.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transaction")
@Tag(name = "TRANSACTION", description = "methods of Transaction APIs")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Create transaction", description = "Create transaction")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successfully created transaction", 
                content = @Content(schema = @Schema(implementation = CommonResponse.class))),
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewTransaction(@RequestBody NewTransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.create(request);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully create transaction")
                .data(transactionResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get transaction by id", description = "Get transaction by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get transaction by id successfully", 
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE ,schema = @Schema(implementation = CommonResponse.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
    })
    @GetMapping(
        path = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTransactionById(@Parameter(description = "ID of transaction to be retrieved", required = true) @PathVariable("id") String id) {
        TransactionResponse transactionResponse = transactionService.get(id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get transaction by id successfully")
                .data(transactionResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update transaction", description = "Update transaction")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update transaction successfully", 
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content)
    })
    @PutMapping(
        path = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateTransaction(@RequestBody UpdateTransactionRequest request,@Parameter(description = "ID of transaction to be retrieved", required = true) @PathVariable("id") String id) {
        TransactionResponse transactionResponse = transactionService.update(request, id);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update transaction")
                .data(transactionResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all transactions", description = "Get all transactions")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get all transactions successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
    })
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllTransactions(@RequestParam(value = "startDate", required = false) String startDate,
                                                @RequestParam(value = "endDate", required = false) String endDate,
                                                @RequestParam(value = "transactionType", required = false) String transactionType,
                                                @RequestParam(value = "productName", required = false) String productName,
                                                @RequestParam(value = "productCode", required = false) String productCode,
                                                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .transactionType(transactionType)
                .productName(productName)
                .productCode(productCode)
                .build();

        Page<TransactionResponse> transactionResponses = transactionService.getAll(request, page, size);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .count(transactionResponses.getTotalElements())
                .page(transactionResponses.getNumberOfElements())
                .totalPage(transactionResponses.getTotalPages())
                .size(transactionResponses.getSize())
                .build();

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all transactions")
                .data(transactionResponses.getContent())
                .pagination(paginationResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get total sales", description = "Get total sales")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get total sales successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
    })
    @GetMapping(
        path = "/total-sales",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTotalSales(@RequestParam(value = "startDate", required = false) String startDate,
                                           @RequestParam(value = "endDate", required = false) String endDate) {
        
        SearchSalesHistoryRequest request = SearchSalesHistoryRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        TotalSales totalSales = transactionService.getSalesHistory(request);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get total sales successfully")
                .data(totalSales)
                .build();

        return ResponseEntity.ok(response);
    }
}
