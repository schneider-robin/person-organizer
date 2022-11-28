package de.schneider.robin.backend.db.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Country {

    DE(Constants.DE_VALUE),
    FR(Constants.FR_VALUE),
    ES(Constants.ES_VALUE);

    private final String name;

    Country(
            final String name
    ) {
        this.name = name;
    }

    @JsonCreator
    public static Country fromValue(
            String name
    ) {
        for (Country action : values()) {
            if (action.name.equalsIgnoreCase(name)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + name + "'");
    }

    @JsonValue
    public String toValue() {
        return this.name;
    }

    public static class Constants {

        public static final String DE_VALUE = "DE";
        public static final String FR_VALUE = "FR";
        public static final String ES_VALUE = "ES";
    }
}
