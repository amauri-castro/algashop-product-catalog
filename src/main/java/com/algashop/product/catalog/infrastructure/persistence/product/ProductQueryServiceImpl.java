package com.algashop.product.catalog.infrastructure.persistence.product;

import com.algashop.product.catalog.application.PageModel;
import com.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algashop.product.catalog.application.product.query.ProductFilter;
import com.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algashop.product.catalog.application.utility.Mapper;
import com.algashop.product.catalog.domain.model.product.Product;
import com.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationExpressionCriteria;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    private final MongoOperations mongoOperations;

    private static final String findWordRegex = "(?i)%s";

    @Override
    public ProductDetailOutput findById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return mapper.convert(product, ProductDetailOutput.class);
    }

    @Override
    public PageModel<ProductSummaryOutput> filter(ProductFilter filter) {
        Query query = queryWith(filter);
        long totalItems = mongoOperations.count(query, Product.class);
        Sort sort = sortWith(filter);

        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        Query pagedQuery = query.with(pageRequest);

        List<Product> products;
        int totalPages = 0;
        if (totalItems > 0) {
            products = mongoOperations.find(pagedQuery, Product.class);
            totalPages = (int) Math.ceil((double) totalItems / pageRequest.getPageSize());
        } else {
            products = new ArrayList<>();
        }

        List<ProductSummaryOutput> productOutputs = products
                .stream()
                .map(p -> mapper.convert(p, ProductSummaryOutput.class))
                .toList();

        return PageModel.<ProductSummaryOutput>builder()
                .content(productOutputs)
                .number(pageRequest.getPageNumber())
                .size(pageRequest.getPageSize())
                .totalPages(totalPages)
                .totalElements(totalItems)
                .build();
    }

    private Sort sortWith(ProductFilter filter) {
        return Sort.by(filter.getSortDirectionOrDefault(),
                filter.getSortByPropertyOrDefault().getPropertyName());
    }

    private Query queryWith(ProductFilter filter) {
        Query query = new Query();
        if (filter.getEnabled() != null) {
            query.addCriteria(Criteria.where("enabled").is(filter.getEnabled()));
        }

        if (filter.getAddedAtFrom() != null & filter.getAddedAtTo() != null) {
            query.addCriteria(Criteria.where("addedAt")
                    .gte(filter.getAddedAtFrom())
                    .lte(filter.getAddedAtTo())
            );
        } else {
            if (filter.getAddedAtFrom() != null) {
                query.addCriteria(Criteria.where("addedAt").gte(filter.getAddedAtFrom()));
            } else if (filter.getAddedAtTo() != null) {
                query.addCriteria(Criteria.where("addedAt").lte(filter.getAddedAtTo()));
            }
        }

        if (filter.getPriceFrom() != null && filter.getPriceTo() != null) {
            query.addCriteria(Criteria.where("salePrice")
                    .gte(filter.getPriceFrom())
                    .lte(filter.getPriceTo())
            );
        } else {
            if (filter.getPriceFrom() != null) {
                query.addCriteria(Criteria.where("salePrice").gte(filter.getPriceFrom()));
            } else if (filter.getPriceTo() != null) {
                query.addCriteria(Criteria.where("salePrice").lte(filter.getPriceTo()));
            }
        }

        if (filter.getHasDiscount() != null) {
            if (filter.getHasDiscount()) {
                query.addCriteria(AggregationExpressionCriteria.whereExpr(
                        ComparisonOperators.valueOf("$salePrice").lessThan("$regularPrice")
                ));
            } else {
                query.addCriteria(AggregationExpressionCriteria.whereExpr(
                        ComparisonOperators.valueOf("$salePrice").equalTo("$regularPrice")
                ));
            }
        }

        if (filter.getInStock() != null) {
            if (filter.getInStock()) {
                query.addCriteria(Criteria.where("quantityInStock").gt(0));
            } else {
                query.addCriteria(Criteria.where("quantityInStock").is(0));

            }
        }

        if (filter.getCategoriesId() != null && filter.getCategoriesId().length > 0) {
            query.addCriteria(Criteria.where("categoryId").in(
                    (Object[]) filter.getCategoriesId()
            ));
        }

        if (StringUtils.isNoneBlank(filter.getTerm())) {
            String regexExpressiong = String.format(findWordRegex, filter.getTerm());
            query.addCriteria(
                    new Criteria().orOperator(
                            Criteria.where("name").regex(regexExpressiong),
                            Criteria.where("brand").regex(regexExpressiong),
                            Criteria.where("description").regex(regexExpressiong)

                    )
            );
        }

        return query;
    }
}
