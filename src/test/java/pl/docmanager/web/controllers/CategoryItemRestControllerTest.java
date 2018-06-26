package pl.docmanager.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.docmanager.domain.CategoryBuilder;
import pl.docmanager.domain.CategoryItemBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.web.service.category.CategoryItemService;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryItemRestController.class)
public class CategoryItemRestControllerTest extends RestControllerTestBase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryItemService categoryItemService;

    @Before
    public void setup() {
        super.setup();
        Solution solution1 = new SolutionBuilder(1).build();
        Category category1 = new CategoryBuilder(1, solution1).withName("exampleCategory").build();
        CategoryItem categoryItem1 = new CategoryItemBuilder(1, category1).build();
        CategoryItem categoryItem2 = new CategoryItemBuilder(2, category1).build();
        CategoryItem categoryItem3 = new CategoryItemBuilder(3, category1).build();

        given(categoryItemService.getAllByCategoryId(1, validToken))
                .willReturn(Arrays.asList(categoryItem1, categoryItem2, categoryItem3));

        Solution solution2 = new SolutionBuilder(2).build();
        Category category2 = new CategoryBuilder(2, solution2).withName("exampleCategory").build();
        CategoryItem categoryItem4 = new CategoryItemBuilder(4, category2).build();
        CategoryItem categoryItem5 = new CategoryItemBuilder(5, category2).build();
        CategoryItem categoryItem6 = new CategoryItemBuilder(6, category2).build();

        given(categoryItemService.getAllByCategoryId(2, validToken))
                .willReturn(Arrays.asList(categoryItem4, categoryItem5, categoryItem6));
        given(categoryItemService.getAllByCategoryId(3, validToken)).willReturn(new ArrayList<>());
    }

    @Test
    public void getAllCategoryItemsByCategoryIdTestValid() throws Exception {
        String expectedJson =
                "[{id: 1, category: {id: 1, solution: {id: 1, state: ACTIVE}, state: ACTIVE}, state: ACTIVE}, " +
                "{id: 2, category: {id: 1, solution: {id: 1, state: ACTIVE}, state: ACTIVE}, state: ACTIVE}, " +
                "{id: 3, category: {id: 1, solution: {id: 1, state: ACTIVE}, state: ACTIVE}, state: ACTIVE}]";
        mvc.perform(get("/api/category_items/category/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getAllCategoryItemsByCategoryIdTestValidEmptyList() throws Exception {
        String expectedJson = "[]";
        mvc.perform(get("/api/category_items/category/3")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}