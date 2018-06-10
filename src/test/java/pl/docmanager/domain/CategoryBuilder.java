package pl.docmanager.domain;

import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryState;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;

import java.time.LocalDateTime;

public class CategoryBuilder {
    private long id;
    private Solution solution;
    private User author;
    private String name;
    private LocalDateTime createDate;
    private String url;
    private CategoryState state;

    public CategoryBuilder(long id, Solution solution) {
        this.id = id;
        this.solution = solution;
        this.state = CategoryState.ACTIVE;
    }

    public CategoryBuilder withAuthor(User author) {
        this.author = author;
        return this;
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder withCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public CategoryBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public CategoryBuilder withState(CategoryState state) {
        this.state = state;
        return this;
    }

    public Category build() {
        Category category = new Category();
        category.setId(id);
        category.setSolution(solution);
        category.setAuthor(author);
        category.setName(name);
        category.setCreateDate(createDate);
        category.setUrl(url);
        category.setState(state);
        return category;
    }
}
