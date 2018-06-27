package pl.docmanager.domain.solution;

import java.time.LocalDateTime;

public class SolutionBuilder {
    private long id;
    private String name;
    private LocalDateTime createDate;
    private SolutionState state;

    public SolutionBuilder(long id) {
        this.id = id;
        state = SolutionState.ACTIVE;
    }

    public SolutionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SolutionBuilder withCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public SolutionBuilder withState(SolutionState state) {
        this.state = state;
        return this;
    }

    public Solution build() {
        Solution solution = new Solution();
        solution.setId(id);
        solution.setName(name);
        solution.setCreateDate(createDate);
        solution.setState(state);
        return solution;
    }
}
