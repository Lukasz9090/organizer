package pl.com.organizer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.com.organizer.validator.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    private List<Expense> expenses = new ArrayList<>();

    private boolean isActive;

    private String confirmationNumber;

    private String resetPasswordNumber;

    public User() {
    }

    public User(String email,
                String password,
                String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getResetPasswordNumber() {
        return resetPasswordNumber;
    }

    public void setResetPasswordNumber(String resetPasswordNumber) {
        this.resetPasswordNumber = resetPasswordNumber;
    }

    public String confirmationNumberGenerator(){
        return UUID.randomUUID().toString();
    }
}
