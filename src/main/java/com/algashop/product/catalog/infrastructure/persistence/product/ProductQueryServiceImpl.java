package com.algashop.product.catalog.infrastructure.persistence.product;

import com.algashop.product.catalog.application.PageModel;
import com.algashop.product.catalog.application.ResourceNotFoundException;
import com.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algashop.product.catalog.application.utility.Mapper;
import com.algashop.product.catalog.domain.model.product.Product;
import com.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Override
    public ProductDetailOutput findById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return mapper.convert(product, ProductDetailOutput.class);
    }

    @Override
    public PageModel<ProductSummaryOutput> filter(Integer size, Integer number) {
        return null;
    }
}
