package pl.docmanager.domain;

import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.domain.user.UserState;
import pl.docmanager.domain.user.UserType;

import java.time.LocalDateTime;

public class UserBuilder {
    private long id;
    private Solution solution;
    private String email;
    private String password;
    private LocalDateTime joinDate;
    private UserState state;
    private User createdBy;
    private UserType userType;

    public UserBuilder(long id, Solution solution) {
        this.id = id;
        this.solution = solution;
        this.state = UserState.ACTIVE;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
        return this;
    }

    public UserBuilder withState(UserState state) {
        this.state = state;
        return this;
    }

    public UserBuilder withCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public UserBuilder withUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setSolution(solution);
        user.setEmail(email);
        user.setPassword(password);
        user.setJoinDate(joinDate);
        user.setState(state);
        user.setCreatedBy(createdBy);
        user.setUserType(userType);
        return user;
    }
}
