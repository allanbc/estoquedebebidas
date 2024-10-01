package com.magis5.estoquedebebidas.application.usecases.validators.interfaces;

import com.magis5.estoquedebebidas.application.usecases.validators.implementations.ExistsIdValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ExistsIdValueValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface ExistsId {
    String message() default "O código informado é nulo ou inválido";
    Class<?> [] groups() default { };
    Class<? extends Payload> [] payload() default { };
    String fieldName();
    Class<?> domainClass();
}
