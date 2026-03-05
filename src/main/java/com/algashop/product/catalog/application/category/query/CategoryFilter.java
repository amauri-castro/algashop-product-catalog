package com.algashop.product.catalog.application.category.query;

import com.algashop.product.catalog.application.utility.SortablePageFilter;
import lombok.*;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryFilter extends SortablePageFilter<CategoryFilter.SortType> {

    private Boolean enabled;

    private String name;

    @Override
    public SortType getSortByPropertyOrDefault() {
        return getSortByProperty() == null ? SortType.NAME : getSortByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        NAME("name");

        private final String propertyName;
    }
}
