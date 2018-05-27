package pl.docmanager.web.security;

public class UserPasswordCredentials {
    private String email;
    private String password;

    public UserPasswordCredentials() {
    }

    public UserPasswordCredentials(String email, String password) {
        this.email = email;
        this.password = password;
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
}
