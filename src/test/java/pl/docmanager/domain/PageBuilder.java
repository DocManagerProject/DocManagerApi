package pl.docmanager.domain;

import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.PageSection;
import pl.docmanager.domain.page.PageState;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PageBuilder {
    private long id;
    private Solution solution;
    private User author;
    private String name;
    private LocalDateTime createDate;
    private String url;
    private PageState state;
    private List<PageSection> sections;

    public PageBuilder(long id, Solution solution) {
        this.id = id;
        this.solution = solution;
        this.state  = PageState.ACTIVE;
        this.sections = new ArrayList<>();
    }

    public PageBuilder withAutor(User author) {
        this.author = author;
        return this;
    }

    public PageBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PageBuilder withCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public PageBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public PageBuilder withState(PageState state) {
        this.state = state;
        return this;
    }

    public PageBuilder withSections(List<PageSection> sections) {
        this.sections = sections;
        return this;
    }

    public Page build() {
        Page page = new Page();
        page.setId(id);
        page.setSolution(solution);
        page.setAuthor(author);
        page.setName(name);
        page.setCreateDate(createDate);
        page.setUrl(url);
        page.setState(state);
        page.setSections(sections);
        return page;
    }
}
