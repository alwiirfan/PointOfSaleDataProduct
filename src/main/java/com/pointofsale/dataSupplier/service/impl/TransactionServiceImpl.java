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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.time.LocalDateTime;

import com.pointofsale.dataSupplier.constant.ETransactionType;
import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewTransactionDetailRequest;
import com.pointofsale.dataSupplier.dto.request.NewTransactionRequest;
import com.pointofsale.dataSupplier.dto.request.SearchSalesHistoryRequest;
import com.pointofsale.dataSupplier.dto.request.SearchTransactionRequest;
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
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(NewTransactionRequest request) {
        // TODO validate request
        validationUtil.validate(request);

        // TODO generate of transaction detail based on request 
        List<TransactionDetail> transactionDetails = new ArrayList<>();

        for (NewTransactionDetailRequest detailRequest : request.getTransactionDetails()) {
            // TODO get product store by product store code
            ProductStore productStore = productStoreService.getByProductCode(detailRequest.getProductCode());

            if (productStore == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(ProductStore.class));
            }

            Integer oldStock = productStore.getProductPrice().getStock();

            // TODO check stock in product store
            if (oldStock < detailRequest.getTotalItem()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock not enough");
            }

            // TODO update stock if stock is enough
            Integer newStock = oldStock - detailRequest.getTotalItem();
            productStoreService.updateProductStoreStock(newStock, productStore.getId());
            
            // TODO calculate total price
            BigDecimal unitPrice = productStore.getProductPrice().getSellingPrice();
            Integer totalItem = detailRequest.getTotalItem();

            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(totalItem));

            // TODO create transaction detail based on request
            TransactionDetail transactionDetail = TransactionDetail.builder()
                                .productStore(productStore)
                                .totalItem(detailRequest.getTotalItem())
                                .totalPrice(totalPrice)
                                .build();

            transactionDetail.setCreatedAt(new Date());

            transactionDetails.add(transactionDetail);
        }

        // TODO create transaction type based on transaction type
        ETransactionType eType = ETransactionType.valueOf(request.getTransactionType().toUpperCase());
        TransactionType transactionType = transactionTypeService.getOrSave(eType);

        transactionType.setCreatedAt(new Date());

        // TODO create transaction and add to transaction details
        Transaction transaction = Transaction.builder()
                .transactionType(transactionType)
                .transactionDate(LocalDateTime.now())
                .transactionDetails(transactionDetails)
                .build();

        transaction.setCreatedAt(new Date());

        // TODO save transaction
        transactionRepository.saveAndFlush(transaction);

        // TODO add transaction to transaction detail
        for (TransactionDetail transactionDetail : transactionDetails) {
            transactionDetail.setTransaction(transaction);
        }

        // TODO return transaction response
        return toTransactionResponse(transaction);
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

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAll(SearchTransactionRequest request, Integer page, Integer size) {
        // TODO create specification
        Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // TODO filter by transaction type
            if (Objects.nonNull(request.getTransactionType())) {
                predicateList.add(criteriaBuilder.equal(root.join("transactionType").get("transactionType"), ETransactionType.getTransactionType(request.getTransactionType().toUpperCase())));
            }

            // TODO filter by product store name
            if (Objects.nonNull(request.getProductName())) {
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("transactionDetails").join("productStore").get("productName")), "%" + request.getProductName().toLowerCase() + "%"));
            }

            // TODO filter by product store code
            if (Objects.nonNull(request.getProductCode())) {
                predicateList.add(criteriaBuilder.like(root.join("transactionDetails").join("productStore").get("productCode"), request.getProductCode()));
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
        BigDecimal cash = transactionRepository.calculateTransactionGroupByTransactionType(ETransactionType.CASH, Objects.nonNull(startDate) ? startDate : LocalDateTime.now(), Objects.nonNull(endDate) ? endDate : LocalDateTime.now());

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
                    .isActive(productStore.getProductPrice().getIsActive())
                    .createdAt(productStore.getCreatedAt().toString())
                    .updatedAt(productStore.getUpdatedAt() != null ? productStore.getUpdatedAt().toString() : null)
                    .build();

            // TODO create transaction detail response return to list 
            return TransactionDetailResponse.builder()
                    .transactionDetailId(transactionDetail.getId())
                    .transactionId(transaction.getId())
                    .productStore(productStoreResponse)
                    .totalItem(transactionDetail.getTotalItem())
                    .totalPrice(transactionDetail.getTotalPrice())
                    .createdAt(transactionDetail.getCreatedAt().toString())
                    .build();
        }).toList();

        // TODO return transaction response
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType().getTransactionType().name())
                .transactionDate(transaction.getTransactionDate().toString())
                .transactionDetails(transactionDetailResponses)
                .createdAt(transaction.getCreatedAt().toString())
                .build();
    }
}
