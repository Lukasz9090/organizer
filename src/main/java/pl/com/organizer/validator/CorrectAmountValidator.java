package pl.com.organizer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CorrectAmountValidator implements ConstraintValidator<CorrectAmount, String> {

    @Override
    public void initialize(CorrectAmount constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern firstValidValueOfAmount = Pattern.compile("-?[0-9]{1,6}\\.[0-9]{0,2}");
        Pattern secondValidValueOfAmount = Pattern.compile("-?[0-9]{1,6}");
        Matcher firstMatcher = firstValidValueOfAmount.matcher(value);
        Matcher secondMatcher = secondValidValueOfAmount.matcher(value);

        return firstMatcher.matches() || secondMatcher.matches();
    }
}
