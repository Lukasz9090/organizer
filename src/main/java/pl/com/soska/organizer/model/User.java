package pl.com.soska.organizer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.com.soska.organizer.validator.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "user")
@FieldMatch(first = "password", second = "confirmPassword", message = "The passwords are different")
public class User {

    @Id
    private String id;

    @Email
    @Indexed(unique = true)
    @NotNull(message = "Please set a e-mail as login")
    private String email;

    @NotNull(message = "Please set a password")
    @Size(min = 6, max = 20, message = "The password should contain between 6 and 20 characters")
    private String password;

    @Transient
    @NotNull(message = "Please confirm your password")
    private String confirmPassword;

    @DBRef
    private Set<Role> role;

    private List<Spending> spending = new ArrayList<>();


    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public List<Spending> getSpending() {
        return spending;
    }

    public void setSpending(List<Spending> spending) {
        this.spending = spending;
    }
}
