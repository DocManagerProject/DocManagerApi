package pl.docmanager.domain.category;

import pl.docmanager.domain.page.Page;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "categoryitem")
public class CategoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "categoryid", nullable = false)
    private Category category;

    @Enumerated
    @Column(name = "contenttype", nullable = false)
    private CategoryItemContentType contentType;

    @ManyToOne
    @JoinColumn(name = "contentpageid")
    private Page contentPage;

    @ManyToOne
    @JoinColumn(name = "contentcategoryid")
    private Category contentCategory;

    @Column(name = "index", nullable = false)
    private int index;

    @Enumerated
    @Column(name = "state", nullable = false)
    private CategoryItemState state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryItemContentType getContentType() {
        return contentType;
    }

    public void setContentType(CategoryItemContentType contentType) {
        this.contentType = contentType;
    }

    public Page getContentPage() {
        return contentPage;
    }

    public void setContentPage(Page contentPage) {
        this.contentPage = contentPage;
    }

    public Category getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Category contentCategory) {
        this.contentCategory = contentCategory;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public CategoryItemState getState() {
        return state;
    }

    public void setState(CategoryItemState state) {
        this.state = state;
    }
}
