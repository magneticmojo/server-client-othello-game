package emailservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Implementation from https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
public class EmailValidator {

    public static boolean isValidEmailAddress(String email) {
        String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
