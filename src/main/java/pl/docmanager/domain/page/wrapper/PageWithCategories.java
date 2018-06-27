package pl.docmanager.domain.page.wrapper;

import pl.docmanager.domain.page.Page;

import java.io.Serializable;
import java.util.List;

public class PageWithCategories implements Serializable {
    private Page page;
    private List<Long> categories;

    public PageWithCategories() { }

    public PageWithCategories(Page page, List<Long> categories) {
        this.page = page;
        this.categories = categories;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }
}
