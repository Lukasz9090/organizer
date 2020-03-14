package pl.com.organizer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectAmountValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectAmount {
    String message() default "Wrong amount format.\n" +
                             "Example of the correct amount: 15.95\n" +
                             "Scope: 1 - 999999 ; 0.01 - 999999.99";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload()default {};
}
