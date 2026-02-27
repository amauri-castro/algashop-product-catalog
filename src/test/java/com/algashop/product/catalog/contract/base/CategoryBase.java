package com.algashop.product.catalog.contract.base;

import com.algashop.product.catalog.application.PageModel;
import com.algashop.product.catalog.application.category.management.CategoryInput;
import com.algashop.product.catalog.application.category.management.CategoryManagementServiceApplicationService;
import com.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algashop.product.catalog.application.category.query.CategoryOutputTestDataBuilder;
import com.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algashop.product.catalog.presentation.CategoryController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CategoryQueryService categoryQueryService;

    @MockitoBean
    private CategoryManagementServiceApplicationService categoryManagementServiceApplicationService;

    public static final UUID validCategoryId = UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2");

    public static final UUID createdCategoryId = UUID.randomUUID();


    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build());

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockFilterCategories();
        mockCreateCategory();
        mockValidCategoryFindById();
    }


    private void mockFilterCategories() {
        Mockito.when(categoryQueryService.filter(
                        Mockito.anyInt(), Mockito.anyInt()))
                .then((answer) -> {
                    Integer size = answer.getArgument(0);

                    return PageModel.<CategoryDetailOutput>builder()
                            .number(0)
                            .size(size)
                            .totalPages(1)
                            .totalElements(2)
                            .content(
                                    List.of(
                                            CategoryOutputTestDataBuilder.aCategory().build(),
                                            CategoryOutputTestDataBuilder.aDisabledCategory().build()
                                    )
                            ).build();
                });
    }

    private void mockCreateCategory() {
        Mockito.when(categoryManagementServiceApplicationService.create(Mockito.any(CategoryInput.class)))
                .thenReturn(createdCategoryId);
        Mockito.when(categoryQueryService.findById(createdCategoryId))
                .thenReturn(CategoryOutputTestDataBuilder.aCategory().id(createdCategoryId).build());
    }

    private void mockValidCategoryFindById() {
        Mockito.when(categoryQueryService.findById(validCategoryId))
                .thenReturn(CategoryOutputTestDataBuilder.aCategory().id(validCategoryId).build());
    }
}
