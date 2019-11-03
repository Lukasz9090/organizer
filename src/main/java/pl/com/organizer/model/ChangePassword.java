package pl.com.organizer.model;

import pl.com.organizer.validator.FieldMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "newPassword", second = "confirmNewPassword", message = "The passwords are different.")
public class ChangePassword {

    private String oldPassword;

    @NotNull(message = "Please set a password")
    @Size(min = 6, max = 20, message = "The password should contain between 6 and 20 characters")
    private String newPassword;

    @NotNull(message = "Please confirm your password")
    private String confirmNewPassword;

    public ChangePassword() {
    }

    public ChangePassword(String oldPassword,
                          String newPassword,
                          String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
