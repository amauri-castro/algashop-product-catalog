package com.algashop.product.catalog.application.category.management;

import com.algashop.product.catalog.application.ApplicationMessagePublisher;
import com.algashop.product.catalog.application.category.event.CategoryUpdateEvent;
import com.algashop.product.catalog.domain.model.category.Category;
import com.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algashop.product.catalog.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryManagementApplicationService {

    private final CategoryRepository categoryRepository;
    private final ApplicationMessagePublisher applicationMessagePublisher;

    public UUID create(CategoryInput input) {
        Category category = new Category(input.getName(), input.getEnabled());
        categoryRepository.save(category);
        return category.getId();
    }

    public void update(UUID categoryId, CategoryInput input) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        category.setName(input.getName());
        category.setEnabled(input.getEnabled());
        categoryRepository.save(category);

        applicationMessagePublisher.send(new CategoryUpdateEvent(
                category.getId(),
                category.getName(),
                category.getEnabled()
        ));

    }

    public void disable(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        category.setEnabled(false);
        categoryRepository.save(category);

        applicationMessagePublisher.send(new CategoryUpdateEvent(
                category.getId(),
                category.getName(),
                category.getEnabled()
        ));
    }
}
