package app.chat.helpers;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Convert {
    public static <T> Set<T> convertArrayToSet(T[] array) {
        return Arrays.stream(array).collect(Collectors.toSet());
    }
}
