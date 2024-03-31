package com.pointofsale.dataSupplier.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

import com.pointofsale.dataSupplier.constant.ETransactionType;
import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewTransactionDetailRequest;
import com.pointofsale.dataSupplier.dto.request.NewTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.SearchSalesHistoryRequest;
import com.pointofsale.dataSupplier.dto.request.SearchTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.UpdateTransactionDetailRequest;
import com.pointofsale.dataSupplier.dto.response.ProductStoreResponse;
import com.pointofsale.dataSupplier.dto.response.TotalSales;
import com.pointofsale.dataSupplier.dto.response.TransactionDetailResponse;
import com.pointofsale.dataSupplier.dto.response.TransactionResponse;
import com.pointofsale.dataSupplier.entity.ProductStore;
import com.pointofsale.dataSupplier.entity.Transaction;
import com.pointofsale.dataSupplier.entity.TransactionDetail;
import com.pointofsale.dataSupplier.entity.TransactionType;
import com.pointofsale.dataSupplier.repository.TransactionRepository;
import com.pointofsale.dataSupplier.service.ProductStoreService;
import com.pointofsale.dataSupplier.service.TransactionService;
import com.pointofsale.dataSupplier.service.TransactionTypeService;
import com.pointofsale.dataSupplier.util.POSUtil;
import com.pointofsale.dataSupplier.util.RandomUtil;
import com.pointofsale.dataSupplier.util.ValidationUtil;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ProductStoreService productStoreService;
    private final TransactionTypeService transactionTypeService;
    private final ValidationUtil validationUtil;
    private final RandomUtil randomUtil;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(NewTransactionRequest request) {
        // TODO validate request
        validationUtil.validate(request);

        // TODO generate of transaction detail based on request 
        List<TransactionDetail> transactionDetails = new ArrayList<>();

        // TODO initialize total price transaction
        BigDecimal totalPriceTransaction = BigDecimal.ZERO;

        for (NewTransactionDetailRequest detailRequest : request.getTransactionDetails()) {
            // TODO get product store by product store code
            ProductStore productStore = productStoreService.getByProductCode(detailRequest.getProductCode());

            if (productStore == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(ProductStore.class));
            }

            // TODO update stock if stock is enough
            Integer newStock = newStock(productStore, detailRequest.getTotalItem());
            productStoreService.updateProductStoreStock(newStock, productStore.getId());
            
            // TODO calculate total price
            BigDecimal totalPrice = calculateTotalPrice(productStore, detailRequest.getTotalItem());

            // TODO create transaction detail based on request
            TransactionDetail transactionDetail = TransactionDetail.builder()
                                .productStore(productStore)
                                .totalItem(detailRequest.getTotalItem())
                                .totalPrice(totalPrice)
                                .build();

            transactionDetail.setCreatedAt(LocalDateTime.now());

            transactionDetails.add(transactionDetail);

            // TODO add total price to total price transaction
            totalPriceTransaction = totalPriceTransaction.add(totalPrice);
        }

        // TODO create transaction type based on transaction type
        ETransactionType eType = ETransactionType.valueOf(request.getTransactionType().toUpperCase());
        TransactionType transactionType = transactionTypeService.getOrSave(eType);

        transactionType.setCreatedAt(LocalDateTime.now());

        // TODO logic for payment and back
        BigDecimal payment = request.getPayment();
        BigDecimal back = payment.subtract(totalPriceTransaction);
        if (back.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment not enough");
        }

        String noStruk = randomUtil.generateRandomNumber();

        // TODO create transaction and add to transaction details
        Transaction transaction = Transaction.builder()
                .transactionType(transactionType)
                .transactionDate(LocalDateTime.now())
                .noStruk(noStruk)
                .payment(payment)
                .back(back)
                .totalPrice(totalPriceTransaction)
                .transactionDetails(transactionDetails)
                .build();

        transaction.setCreatedAt(LocalDateTime.now());

        // TODO save transaction
        transactionRepository.saveAndFlush(transaction);

        // TODO add transaction to transaction detail
        for (TransactionDetail transactionDetail : transactionDetails) {
            transactionDetail.setTransaction(transaction);
        }

        // TODO return transaction response
        return toTransactionResponse(transaction);
    }

    private Integer newStock(ProductStore productStore, Integer totalItem) {
        Integer oldStock = productStore.getProductPrice().getStock();

        if (oldStock < totalItem) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock not enough");
        }

        Integer newStock = oldStock - totalItem;
        return newStock;
    }

    private BigDecimal calculateTotalPrice(ProductStore productStore, Integer totalItem) {
        BigDecimal unitPrice = productStore.getProductPrice().getSellingPrice();

        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(totalItem));
        return totalPrice;
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse get(String transactionId) {
        // TODO get transaction by id
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(Transaction.class)));
        
        return toTransactionResponse(transaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse update(UpdateTransactionRequest request, String id) {
        // TODO validate request
        validationUtil.validate(request);
        
        // TODO get transaction by id
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        ResponseMessage.getNotFoundResource(Transaction.class)));

        // TODO check date is now 
        if (!checkDate(transaction.getTransactionDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Transaction can't be update");
        }

        // TODO create map of detail request
        Map<String, UpdateTransactionDetailRequest> detailRequestMap = request.getTransactionDetails().stream()
                .collect(Collectors.toMap(UpdateTransactionDetailRequest::getTransactionDetailId, Function.identity()));

        // TODO get transaction details
        List<TransactionDetail> transactionDetails = transaction.getTransactionDetails();

        // TODO initialize total price transaction
        BigDecimal totalPriceTransaction = BigDecimal.ZERO;

        // TODO update transaction details using foreach
            for (TransactionDetail transactionDetail : transactionDetails) {
                String transactionDetailId = transactionDetail.getId();
                
                // TODO check detail request exist in detail request
                if (detailRequestMap.containsKey(transactionDetailId)) {
                    UpdateTransactionDetailRequest detailRequest = detailRequestMap.get(transactionDetailId);

                    // TODO get product store by product store code
                    ProductStore productStore = productStoreService.getByProductCode(detailRequest.getProductCode());

                    // TODO Rollback stock change based on previous total item
                    rollbackStockChange(transactionDetail);

                    // TODO Update stock to new total item
                    if (detailRequest.getNewTotalItem() != null) {
                        Integer newStock = newStock(productStore, detailRequest.getNewTotalItem());
                        productStoreService.updateProductStoreStock(newStock, productStore.getId());
                    }

                    // TODO Calculate new total price
                    BigDecimal newTotalPrice = calculateTotalPrice(productStore, detailRequest.getNewTotalItem());

                    // TODO Update transaction detail
                    transactionDetail.setTotalItem(detailRequest.getNewTotalItem());
                    transactionDetail.setTotalPrice(newTotalPrice);
                    transactionDetail.setUpdatedAt(LocalDateTime.now());

                    totalPriceTransaction = totalPriceTransaction.add(newTotalPrice);
                }
            }

        // TODO create transaction type based on transaction type
        ETransactionType eType = ETransactionType.valueOf(request.getTransactionType().toUpperCase());
        TransactionType transactionType = transactionTypeService.getOrSave(eType);
        transactionType.setCreatedAt(LocalDateTime.now());

        // TODO logic for payment and back
        BigDecimal payment = request.getPayment();
        BigDecimal back = payment.subtract(totalPriceTransaction);
        if (back.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment not enough");
        }

        // TODO create transaction and add to transaction details
        transaction.setTransactionType(transactionType);
        transaction.setPayment(payment);
        transaction.setBack(back);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTotalPrice(totalPriceTransaction);
        transaction.setTransactionDetails(transactionDetails);
        transaction.setUpdatedAt(LocalDateTime.now());

        // TODO save transaction
        transactionRepository.saveAndFlush(transaction);

        // TODO add transaction to transaction detail
        for (TransactionDetail transactionDetail : transactionDetails) {
            transactionDetail.setTransaction(transaction);
        }

        // TODO return transaction response
        return toTransactionResponse(transaction);

    }

    private void rollbackStockChange(TransactionDetail transactionDetail) {
        ProductStore productStore = transactionDetail.getProductStore();
        Integer previousStock = productStore.getProductPrice().getStock();
        Integer currentStock = previousStock + transactionDetail.getTotalItem();
        productStoreService.updateProductStoreStock(currentStock, productStore.getId());
    }

    private Boolean checkDate(LocalDateTime transactionDateFromDatabase) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateFromDatabase = transactionDateFromDatabase.toLocalDate();

        return currentDate.equals(dateFromDatabase);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAll(SearchTransactionRequest request, Integer page, Integer size) {
        // TODO create specification
        Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // TODO filter by transaction type
            if (Objects.nonNull(request.getTransactionType())) {
                predicateList.add(criteriaBuilder.equal(root.join("transactionType")
                        .get("transactionType"), 
                                ETransactionType.getTransactionType(request.getTransactionType().toUpperCase())));
            }

            // TODO filter by product store name
            if (Objects.nonNull(request.getProductName())) {
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("transactionDetails")
                        .join("productStore")
                                .get("productName")), "%" + request.getProductName().toLowerCase() + "%"));
            }

            // TODO filter by product store code
            if (Objects.nonNull(request.getProductCode())) {
                predicateList.add(criteriaBuilder.like(root.join("transactionDetails")
                        .join("productStore")
                                .get("productCode"), request.getProductCode()));
            }

            // TODO filter by start date and end date
            if (Objects.nonNull(request.getStartDate()) && Objects.nonNull(request.getEndDate())) {
                LocalDateTime startDate = POSUtil.parseLocalDateTime(request.getStartDate());
                LocalDateTime endDate = POSUtil.parseLocalDateTime(request.getEndDate());
                predicateList.add(criteriaBuilder.between(root.get("transactionDate"), startDate, endDate));
            }

            // TODO filter by start date if end date is null
            if (Objects.nonNull(request.getStartDate()) && Objects.isNull(request.getEndDate())) {
                LocalDateTime startDate = POSUtil.parseLocalDateTime(request.getStartDate());
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), startDate));
            }

            return query.where(predicateList.toArray(new Predicate[]{})).getRestriction();
        };

        // TODO create pageable
        Pageable pageable = PageRequest.of(page, size);

        // TODO get all transactions
        Page<Transaction> transactions = transactionRepository.findAll(specification, pageable);

        // TODO map transaction responses to list
        List<TransactionResponse> transactionList = transactions.stream().map(this::toTransactionResponse).toList();
        
        return new PageImpl<>(transactionList, pageable, transactions.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public TotalSales getSalesHistory(SearchSalesHistoryRequest request) {
        // TODO initialize start date and end date
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        // TODO filter by start date
        if (Objects.nonNull(request.getStartDate())) {
            startDate = POSUtil.parseLocalDateTime(request.getStartDate());
        }

        // TODO filter by end date
        if (Objects.nonNull(request.getEndDate())) {
            endDate = POSUtil.parseLocalDateTime(request.getEndDate());
        }

        // TODO calculate total sales
        BigDecimal cash = transactionRepository
                .calculateTransactionGroupByTransactionType(ETransactionType.CASH, Objects.nonNull(startDate) ? 
                        startDate : LocalDateTime.now(), Objects.nonNull(endDate) ? 
                                endDate : LocalDateTime.now());

        // TODO return total sales
        return TotalSales.builder()
                .cash(cash)
                .build();
    }
    
    // TODO convert transaction to transaction response
    private TransactionResponse toTransactionResponse(Transaction transaction) {
        // TODO create transaction detail responses based on transaction
        List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream().map(transactionDetail -> {

            // TODO create product store response
            ProductStore productStore = transactionDetail.getProductStore();

            ProductStoreResponse productStoreResponse = ProductStoreResponse.builder()
                    .productStoreId(productStore.getId())
                    .productCode(productStore.getProductCode())
                    .productName(productStore.getProductName())
                    .productCategory(productStore.getCategory().getCategory())
                    .productDescription(productStore.getDescription())
                    .productMerk(productStore.getMerk())
                    .productPurchasePrice(productStore.getProductPrice().getPurchasePrice())
                    .productSellingPrice(productStore.getProductPrice().getSellingPrice())
                    .productStock(productStore.getProductPrice().getStock())
                    .createdAt(productStore.getCreatedAt().toString())
                    .updatedAt(productStore.getUpdatedAt() != null ? productStore.getUpdatedAt().toString() : null)
                    .build();

            // TODO create transaction detail response return to list 
            return TransactionDetailResponse.builder()
                    .transactionDetailId(transactionDetail.getId())
                    .transactionId(transaction.getId())
                    .totalItem(transactionDetail.getTotalItem())
                    .totalPrice(transactionDetail.getTotalPrice())
                    .createdAt(transactionDetail.getCreatedAt().toString())
                    .updatedAt(transactionDetail.getUpdatedAt() != null ? 
                            transactionDetail.getUpdatedAt().toString() : null)
                    .productStore(productStoreResponse)
                    .build();
        }).toList();

        // TODO return transaction response
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType().getTransactionType().name())
                .noStruk(transaction.getNoStruk())
                .transactionDate(transaction.getTransactionDate().toString())
                .payment(transaction.getPayment())
                .back(transaction.getBack())
                .totalPrice(transaction.getTotalPrice())
                .createdAt(transaction.getCreatedAt().toString())
                .updatedAt(transaction.getUpdatedAt() != null ? 
                        transaction.getUpdatedAt().toString() : null)
                .transactionDetails(transactionDetailResponses)
                .build();
    }

}
