package pl.com.soska.organizer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PastLocalDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PastLocalDate {
    String message() default "The date should be from today or from the past";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload()default {};
}
