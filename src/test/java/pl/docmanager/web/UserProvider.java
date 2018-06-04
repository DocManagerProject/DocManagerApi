package pl.docmanager.web;

import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.domain.user.UserState;

public class UserProvider {
    public static User getMockUser(long userId, long solutionId, String userEmail, UserState state, String password) {
        Solution solution = new Solution();
        solution.setId(solutionId);

        User user = new User();
        user.setId(userId);
        user.setSolution(solution);
        user.setEmail(userEmail);
        user.setPassword(password);
        user.setState(state);
        return user;
    }

    public static User getMockUser(long userId, long solutionId, String userEmail, UserState state) {
        return getMockUser(userId, solutionId, userEmail, state, null);
    }
}
