package pl.docmanager.domain.page;

import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "solutionid", nullable = false)
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "authorid", nullable = false)
    private User author;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "createdate", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "url", length = 256, nullable = false)
    private String url;

    @Enumerated
    @Column(name = "state", nullable = false)
    private PageState state;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "page")
    @OrderBy("index desc")
    private List<PageSection> sections;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PageState getState() {
        return state;
    }

    public void setState(PageState state) {
        this.state = state;
    }

    public List<PageSection> getSections() {
        return sections;
    }

    public void setSections(List<PageSection> sections) {
        this.sections = sections;
    }
}
