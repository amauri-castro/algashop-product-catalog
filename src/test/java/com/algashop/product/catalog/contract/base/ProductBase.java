package com.algashop.product.catalog.contract.base;

import com.algashop.product.catalog.application.PageModel;
import com.algashop.product.catalog.application.ResourceNotFoundException;
import com.algashop.product.catalog.application.product.management.ProductInput;
import com.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.algashop.product.catalog.application.product.query.*;
import com.algashop.product.catalog.presentation.ProductController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = ProductController.class)
public class ProductBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ProductQueryService productQueryService;

    @MockitoBean
    private ProductManagementApplicationService productManagementApplicationService;

    public static final UUID validProductId = UUID.fromString("019be330-5c35-7ef8-b59b-0cf73765a296");
    public static final UUID invalidProductId = UUID.fromString("019bff63-8a93-7884-89aa-cf73116430ed");



    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build());

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockValidProductFindById();
        mockFilterProducts();
        mockCreateProduct();
        mockInvalidProductFindById();
        mockInvalidProductDeleteById();
    }

    private void mockValidProductFindById() {
        Mockito.when(productQueryService.findById(validProductId))
                .thenReturn(ProductDetailOutputTestDataBuilder.aProduct().id(validProductId).build());
    }

    private void mockFilterProducts() {
        Mockito.when(productQueryService.filter(
                        Mockito.anyInt(), Mockito.anyInt()))
                .then((answer) -> {
                    Integer size = answer.getArgument(0);

                    return PageModel.<ProductDetailOutput>builder()
                            .number(0)
                            .size(size)
                            .totalPages(1)
                            .totalElements(2)
                            .content(
                                    List.of(
                                            ProductDetailOutputTestDataBuilder.aProduct().build(),
                                            ProductDetailOutputTestDataBuilder.aProductAlt1().build()
                                    )
                            ).build();
                });
    }

    private void mockCreateProduct() {
        Mockito.when(productManagementApplicationService.create(Mockito.any(ProductInput.class)))
                .thenReturn(validProductId);
    }

    private void mockInvalidProductFindById() {
        Mockito.when(productQueryService.findById(invalidProductId))
                .thenThrow(new ResourceNotFoundException());
    }

    private void mockInvalidProductDeleteById() {
        Mockito.doThrow(new ResourceNotFoundException()).when(productManagementApplicationService).disable(invalidProductId);
    }

}
