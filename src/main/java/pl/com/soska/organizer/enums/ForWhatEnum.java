package pl.com.soska.organizer.enums;

public enum ForWhatEnum {
    FOOD("food"),
    CLOTHES("clothes"),
    COSMETICS("cosmetics"),
    FLAT_BILLS("flat-bills"), FLAT_OTCHERS("flat-other"),
    CAR_FUEL("car-fuel"), CAR_OTHERS("car-others"),
    RECREATION("recreation"), TRAVEL("travel"),
    ALL("all");

    String description;

    ForWhatEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
