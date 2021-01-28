package com.carfix.serviceplatform.core.data;

public interface EntityExtractor<T, U extends EntityProvider<T>> {

    T extract(U dto);
}
