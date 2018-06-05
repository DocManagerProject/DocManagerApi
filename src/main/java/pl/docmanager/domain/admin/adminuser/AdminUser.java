package pl.docmanager.domain.admin.adminuser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "adminuser", schema = "admin")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", length = 1024, nullable = false)
    private String email;

    @Column(name = "password", length = 2048, nullable = false)
    private String password;

    @Column(name = "joindate", nullable = false)
    private LocalDateTime joinDate;

    @Enumerated
    @Column(name = "state", nullable = false)
    private AdminUserState state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public AdminUserState getState() {
        return state;
    }

    public void setState(AdminUserState state) {
        this.state = state;
    }
}
