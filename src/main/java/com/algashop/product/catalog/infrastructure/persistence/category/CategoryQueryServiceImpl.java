package com.algashop.product.catalog.infrastructure.persistence.category;

import com.algashop.product.catalog.application.PageModel;
import com.algashop.product.catalog.application.ResourceNotFoundException;
import com.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algashop.product.catalog.application.utility.Mapper;
import com.algashop.product.catalog.domain.model.category.Category;
import com.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algashop.product.catalog.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Override
    public CategoryDetailOutput findById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return mapper.convert(category, CategoryDetailOutput.class);
    }

    @Override
    public PageModel<CategoryDetailOutput> filter(Integer size, Integer number) {
        return null;
    }
}
