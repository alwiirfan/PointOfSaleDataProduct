package com.pointofsale.dataSupplier.service.impl;

import com.pointofsale.dataSupplier.constant.ResponseMessage;
import com.pointofsale.dataSupplier.dto.request.NewCategoryRequest;
import com.pointofsale.dataSupplier.dto.response.CategoryResponse;
import com.pointofsale.dataSupplier.entity.Category;
import com.pointofsale.dataSupplier.repository.CategoryRepository;
import com.pointofsale.dataSupplier.service.CategoryService;
import com.pointofsale.dataSupplier.util.ValidationUtil;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

    @Service
    @RequiredArgsConstructor
    public class CategoryServiceImpl implements CategoryService {

        private final CategoryRepository categoryRepository;
        private final ValidationUtil validationUtil;

        @Override
        public CategoryResponse saveCategory(NewCategoryRequest request) {
            validationUtil.validate(request);
            try {
                Category category = Category.builder()
                        .category(request.getCategory().toUpperCase())
                        .build();

                category.setCreatedAt(new Date());
                category.setUpdatedAt(null);

                categoryRepository.save(category);

                return toCategoryResponse(category);
            } catch (ResponseStatusException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.getInternalServerError(Category.class), e);
            }
        }

    @Override
    public Category getCategoryByECategory(String category) {
        return categoryRepository
                .findFirstByCategory(category)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            ResponseMessage.getNotFoundResource(Category.class)));

    }

    @Override
    public Page<CategoryResponse> findAllCategories(String category, Integer page, Integer size) {
        Specification<Category> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (Objects.nonNull(category)) {
                predicateList.add(criteriaBuilder.equal(root.get("category"), "%" + category + "%"));
            }

            return query.where(predicateList.toArray(new Predicate[]{})).getRestriction();
            };

        Pageable pageable = PageRequest.of(page, size);
        
        Page<Category> categories = categoryRepository.findAll(specification, pageable);

        List<CategoryResponse> categoryList = categories.stream().map(this::toCategoryResponse).collect(Collectors.toList());
        
        return new PageImpl<>(categoryList, pageable, categories.getTotalElements());
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                                .categoryId(category.getId())
                                .category(category.getCategory())
                                .createdAt(category.getCreatedAt().toString())
                                .updatedAt(category.getUpdatedAt().toString())
                                .build();
    }
}
