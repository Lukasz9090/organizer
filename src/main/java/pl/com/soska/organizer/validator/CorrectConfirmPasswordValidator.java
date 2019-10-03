package pl.com.soska.organizer.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CorrectConfirmPasswordValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        Object firstObj = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
        Object secondObj = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);

        boolean isValid = firstObj == null && secondObj == null ||
                firstObj != null && firstObj.equals(secondObj);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(secondFieldName)
                    .addConstraintViolation();
        }

        return isValid;
    }
}
