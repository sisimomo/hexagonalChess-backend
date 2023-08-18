package com.github.sisimomo.hexagonalchess.backend.commons.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE,
    ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Documented
public @interface NullOrNotBlank {
  int value() default 0;

  String message() default "Value should be null or not blank";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
