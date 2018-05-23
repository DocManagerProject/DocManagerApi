package pl.docmanager.domain.image;

import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "solutionid", nullable = false)
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "authorid", nullable = false)
    private User author;

    @Column(name = "createdate", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "contenturl", length = 2048, nullable = false)
    private String contentUrl;

    @Column(name = "url", length = 256, nullable = false)
    private String url;

    @Enumerated
    @Column(name = "state", nullable = false)
    private ImageState state;

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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageState getState() {
        return state;
    }

    public void setState(ImageState state) {
        this.state = state;
    }
}
