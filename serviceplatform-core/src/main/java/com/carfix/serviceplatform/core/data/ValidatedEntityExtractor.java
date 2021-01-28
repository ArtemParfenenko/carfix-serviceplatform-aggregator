package com.carfix.serviceplatform.core.data;

public interface ValidatedEntityExtractor <T, U extends EntityProvider<T>> extends EntityExtractor<T, U> {

    default void validate(T entity) throws RuntimeException {
    }

    default void validate(U entityProvider) throws RuntimeException {
    }

    default void validate(T entity, U entityProvider) throws RuntimeException {
        validate(entity);
        validate(entityProvider);
    }
}
