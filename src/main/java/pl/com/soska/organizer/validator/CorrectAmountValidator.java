package pl.com.soska.organizer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CorrectAmountValidator implements ConstraintValidator<AmountValidator, BigDecimal> {

    @Override
    public void initialize(AmountValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        String newValue = value.toString();
        Pattern firstValidValueOfAmount = Pattern.compile("[0-9]{1,6}\\.[0-9]{0,2}");
        Pattern secondValidValueOfAmount = Pattern.compile("[0-9]{1,6}");
        Matcher firstMatcher = firstValidValueOfAmount.matcher(newValue);
        Matcher secondMatcher = secondValidValueOfAmount.matcher(newValue);

        return firstMatcher.matches() || secondMatcher.matches();
    }
}
