package pl.docmanager.domain.page;

import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;

import java.time.LocalDateTime;

public class PageBuilder {
    private long id;
    private Solution solution;
    private User author;
    private String name;
    private String content;
    private LocalDateTime createDate;
    private String url;
    private PageState state;

    public PageBuilder(long id, Solution solution) {
        this.id = id;
        this.solution = solution;
        this.state  = PageState.ACTIVE;
    }

    public PageBuilder withAutor(User author) {
        this.author = author;
        return this;
    }

    public PageBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PageBuilder withContent(String content) {
        this.content = content;
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

    public Page build() {
        Page page = new Page();
        page.setId(id);
        page.setSolution(solution);
        page.setAuthor(author);
        page.setName(name);
        page.setContent(content);
        page.setCreateDate(createDate);
        page.setUrl(url);
        page.setState(state);
        return page;
    }
}
