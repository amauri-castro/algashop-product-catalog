package com.algashop.product.catalog.infrastructure.listener.category;

import com.algashop.product.catalog.application.category.event.CategoryUpdateEvent;
import com.algashop.product.catalog.infrastructure.persistence.category.ProductCategoryUpdater;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CategoryEventListener {

    private final ProductCategoryUpdater productCategoryUpdater;

    @EventListener
    @Async
    public void handle(CategoryUpdateEvent categoryUpdateEvent) {
        productCategoryUpdater.copyCategoryDataToProducts(categoryUpdateEvent);
        log.info("Category updated received: {}", categoryUpdateEvent.getCategoryId());
    }
}
