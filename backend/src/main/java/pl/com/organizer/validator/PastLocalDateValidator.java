package pl.com.organizer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PastLocalDateValidator implements ConstraintValidator<PastLocalDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null){
            return false;
        }
        if (value.isAfter(LocalDate.now())){
            return false;
        }
        return true;
    }
}
