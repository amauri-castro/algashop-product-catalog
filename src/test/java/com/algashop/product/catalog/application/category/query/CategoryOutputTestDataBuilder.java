package com.algashop.product.catalog.application.category.query;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CategoryOutputTestDataBuilder {

    private CategoryOutputTestDataBuilder() {
    }

    public static CategoryDetailOutput.CategoryDetailOutputBuilder aCategory() {
        return CategoryDetailOutput.builder()
                .id(UUID.randomUUID())
                .name("Notebooks")
                .updatedAt(OffsetDateTime.now())
                .enabled(true);
    }

    public static CategoryDetailOutput.CategoryDetailOutputBuilder aDisabledCategory() {
        return CategoryDetailOutput.builder()
                .id(UUID.randomUUID())
                .name("Desktops")
                .updatedAt(OffsetDateTime.now())
                .enabled(false);
    }
}