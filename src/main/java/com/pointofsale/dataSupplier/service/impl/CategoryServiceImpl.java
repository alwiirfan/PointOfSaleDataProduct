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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

    @Service
    @RequiredArgsConstructor
    public class CategoryServiceImpl implements CategoryService {

        private final CategoryRepository categoryRepository;
        private final ValidationUtil validationUtil;

        @Transactional(rollbackFor = Exception.class)
        @Override
        public CategoryResponse saveCategory(NewCategoryRequest request) {
            // TODO validate request
            validationUtil.validate(request);
            try {
                Category category = Category.builder()
                        .category(request.getCategory().toUpperCase())
                        .build();

                category.setCreatedAt(new Date());

                categoryRepository.save(category);

                // TODO return category response
                return toCategoryResponse(category);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                        ResponseMessage.getInternalServerError(Category.class), e);
            }
        }

    
    @Override
    public Category getCategoryByCategory(String category) {
        // TODO get category by category
        return categoryRepository
                .findFirstByCategory(category)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            ResponseMessage.getNotFoundResource(Category.class)));

    }

    @Transactional(readOnly = true)
    @Override
    public Page<CategoryResponse> findAllCategories(String category, Integer page, Integer size) {
        // TODO create specification 
        Specification<Category> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // TODO filter by category
            if (Objects.nonNull(category)) {
                predicateList.add(criteriaBuilder.equal(root.get("category"), "%" + category + "%"));
            }

            return query.where(predicateList.toArray(new Predicate[]{})).getRestriction();
            };

        // TODO create pageable
        Pageable pageable = PageRequest.of(page, size);
        
        // TODO get all categories
        Page<Category> categories = categoryRepository.findAll(specification, pageable);

        // TODO map category responses to list
        List<CategoryResponse> categoryList = categories.stream().map(this::toCategoryResponse).collect(Collectors.toList());
        
        return new PageImpl<>(categoryList, pageable, categories.getTotalElements());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CategoryResponse updateCategory(String id, NewCategoryRequest request) {
        // TODO validate request
        validationUtil.validate(request);

        try {
            // TODO get category by id
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                            ResponseMessage.getNotFoundResource(Category.class)));

            // TODO update category
            String categoryString = request.getCategory();
            if (categoryString.isEmpty() || categoryString.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        ResponseMessage.getBadRequest(Category.class));
            }

            category.setCategory(categoryString.toUpperCase());
            category.setUpdatedAt(new Date());

            categoryRepository.save(category);

            // TODO return category response
            return toCategoryResponse(category);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.getInternalServerError(Category.class), e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCategory(String id) {
        // TODO get category by id
        Category category = categoryRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                                        ResponseMessage.getNotFoundResource(Category.class)));

        // TODO delete category
        categoryRepository.delete(category);
    }

    // TODO create category response
    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                                .categoryId(category.getId())
                                .category(category.getCategory())
                                .createdAt(category.getCreatedAt().toString())
                                .updatedAt(category.getUpdatedAt() != null ? category.getUpdatedAt().toString() : null)
                                .build();
    }

}
