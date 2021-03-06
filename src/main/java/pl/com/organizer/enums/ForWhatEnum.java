package pl.com.organizer.enums;

public enum ForWhatEnum {
    FOOD("food"),
    CLOTHES("clothes"),
    COSMETICS("cosmetics"),
    FLAT_BILLS("flat-bills"), FLAT_OTHER("flat-other"),
    CAR_FUEL("car-fuel"), CAR_OTHER("car-other"),
    RECREATION("recreation"), TRAVELING("traveling"),
    ALL("all");

    String description;

    ForWhatEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
