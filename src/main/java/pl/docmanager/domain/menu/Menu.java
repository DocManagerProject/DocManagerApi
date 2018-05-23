package pl.docmanager.domain.menu;

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
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "solutionid", nullable = false)
    private Solution solution;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "createdby", nullable = false)
    private User createdBy;

    @Column(name = "createdate", nullable = false)
    private LocalDateTime createDate;

    @Enumerated
    @Column(name = "state", nullable = false)
    private MenuState state;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public MenuState getState() {
        return state;
    }

    public void setState(MenuState state) {
        this.state = state;
    }
}
