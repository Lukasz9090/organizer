package pl.com.organizer.enums;

public enum RoleEnum {
    ROLE_USER("ROLE_USER");

    String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
