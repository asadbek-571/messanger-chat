package app.chat.enums;

import java.util.Objects;

public enum ErrorSeries {
    INFORMATIONAL(1),
    CLIENT_ERROR(2),
    SERVER_ERROR(3);

    private final Integer value;

    ErrorSeries(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static ErrorSeries getErrorSeries(int value) {
        for (ErrorSeries series : values()) {
            if (series.getValue() == value) {
                return series;
            }
        }
        return CLIENT_ERROR;
    }

    public static boolean isExist(Integer value) {
        for (ErrorSeries series : values()) {
            if (Objects.equals(series.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotExist(Integer value) {
        for (ErrorSeries series : values()) {
            if (Objects.equals(series.getValue(), value)) {
                return false;
            }
        }
        return true;
    }
}
