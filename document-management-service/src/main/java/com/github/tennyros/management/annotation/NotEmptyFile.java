package com.github.tennyros.management.annotation;

import com.github.tennyros.management.validator.NotEmptyFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NotEmptyFileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyFile {
    String message() default "Uploaded file must not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
