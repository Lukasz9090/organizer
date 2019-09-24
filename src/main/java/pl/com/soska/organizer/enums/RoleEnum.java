package pl.com.soska.organizer.enums;

public enum RoleEnum {
    USER("User");

    String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
