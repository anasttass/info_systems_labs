package ru.stud.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER, RECORD_COMPONENT, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = BcryptPasswordValidator.class)
public @interface ValidBcryptPassword {
    String message() default "Пароль должен занимать не более 72 байт UTF-8";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
