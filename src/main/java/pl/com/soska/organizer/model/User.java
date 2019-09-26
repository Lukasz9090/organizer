package pl.com.soska.organizer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "user")
public class User {

    @Id
    private String id;
    @Email
    @Indexed(unique = true)
    private String email;
    @Size(min = 6, message = "The password should have a minimum 6 characters.")
    private String password;

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
