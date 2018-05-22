package pl.docmanager.domain.solution;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 1024, nullable = false)
    private String name;

    @Column(name = "createdate", nullable = false)
    private LocalDateTime createDate;

    @Enumerated
    @Column(name = "state", nullable = false)
    private SolutionState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SolutionState getState() {
        return state;
    }

    public void setState(SolutionState state) {
        this.state = state;
    }
}
