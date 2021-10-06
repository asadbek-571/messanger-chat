package app.chat.helpers;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    @Value("${upload.path.video}")
    private static String videoPath;
    @Value("${upload.path.music}")
    private static String musicPath;
    @Value("${upload.path.document}")
    private static String documentPath;
    @Value("${upload.path.image}")
    private static String imagePath;
    @Value("${upload.path}")
    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    /**
     * Check for String value
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Check for Object value
     */
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
     * Check for List value
     */
    public static boolean isEmpty(List<?> items) {
        return items == null || items.isEmpty();
    }


    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static LocalDate localDate(String date) {
        return LocalDate.parse(date);
    }

    public static LocalDateTime localDateTime(String dateAndTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dateAndTime, dateTimeFormatter);
    }


    public static String shortDate(String dateAndTime) {
//        String dateAndTime = " 2011-01-18 00:00:00.0";
        try {
            SimpleDateFormat beforeDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = beforeDate.parse(dateAndTime);
            SimpleDateFormat afterDate = new SimpleDateFormat("dd-MM-yyyy");
            return afterDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = null;
        if (phoneNumber.charAt(0) == '+' && phoneNumber.length() == 13)
            pattern = Pattern.compile("^(\\+\\d{1,13})$");
        else if (phoneNumber.charAt(0) != '+' && phoneNumber.length() == 12)
            pattern = Pattern.compile("^\\d{12}$");
        else if (phoneNumber.charAt(0) != '+' && phoneNumber.length() == 9)
            pattern = Pattern.compile("^\\d{9}$");
        else return false;

        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();

    }

}
