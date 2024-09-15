package com.magis5.estoquedebebidas.domain.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

public class ExistsIdValueValidator implements ConstraintValidator<ExistsId, Object> {

    private String domainAttribute;
    private Class<?> klass;
    @PersistenceContext
    private final EntityManager manager;

    public ExistsIdValueValidator(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void initialize(ExistsId params) {
        domainAttribute = params.fieldName();
        klass = params.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(Objects.isNull(value)) return true;
        Query query = manager.createQuery("select 1 from " +klass.getName() + " where " +domainAttribute+"=:value");
        query.setParameter("value", value);
        List<?> list = query.getResultList();
        Assert.isTrue(list.size() <=1, "Foi encontrado mais de um " + klass+ "com o atributo " +domainAttribute+"=+value");
        return !list.isEmpty();
    }
}
