package com.algashop.product.catalog.presentation;

import com.algashop.product.catalog.application.category.management.CategoryInput;
import com.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algashop.product.catalog.application.category.query.CategoryFilter;
import com.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algashop.product.catalog.application.PageModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryQueryService categoryQueryService;
    private final CategoryManagementApplicationService categoryManagementApplicationService;

    @GetMapping
    public PageModel<CategoryDetailOutput> filter(CategoryFilter categoryFilter) {
        return categoryQueryService.filter(categoryFilter);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDetailOutput create(@RequestBody @Valid CategoryInput input) {
        UUID categoryId = categoryManagementApplicationService.create(input);
        return categoryQueryService.findById(categoryId);
    }

    @GetMapping("/{categoryId}")
    public CategoryDetailOutput findById(@PathVariable UUID categoryId) {
        return categoryQueryService.findById(categoryId);
    }

    @PutMapping("/{categoryId}")
    public CategoryDetailOutput update(@PathVariable UUID categoryId,
                                       @RequestBody @Valid CategoryInput input) {
        categoryManagementApplicationService.update(categoryId, input);
        return categoryQueryService.findById(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID categoryId) {
        categoryManagementApplicationService.disable(categoryId);
    }

}
