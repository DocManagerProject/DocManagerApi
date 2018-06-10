package pl.docmanager.domain;

import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.category.CategoryItemContentType;
import pl.docmanager.domain.category.CategoryItemState;
import pl.docmanager.domain.page.Page;

public class CategoryItemBuilder {
    private long id;
    private Category category;
    private CategoryItemContentType contentType;
    private Page contentPage;
    private Category contentCategory;
    private int index;
    private CategoryItemState state;

    public CategoryItemBuilder(long id, Category category) {
        this.id = id;
        this.category = category;
        this.state = CategoryItemState.ACTIVE;
    }

    public CategoryItemBuilder withContentType(CategoryItemContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public CategoryItemBuilder withContentPage(Page contentPage) {
        this.contentPage = contentPage;
        return this;
    }

    public CategoryItemBuilder withContentCategory(Category contentCategory) {
        this.contentCategory = contentCategory;
        return this;
    }

    public CategoryItemBuilder withIndex(int index) {
        this.index = index;
        return this;
    }

    public CategoryItemBuilder withState(CategoryItemState state) {
        this.state = state;
        return this;
    }

    public CategoryItem build() {
        CategoryItem categoryItem = new CategoryItem();
        categoryItem.setId(id);
        categoryItem.setCategory(category);
        categoryItem.setContentType(contentType);
        categoryItem.setContentPage(contentPage);
        categoryItem.setContentCategory(contentCategory);
        categoryItem.setIndex(index);
        categoryItem.setState(state);
        return categoryItem;
    }
}
