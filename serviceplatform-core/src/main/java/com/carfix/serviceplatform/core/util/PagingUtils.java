package com.carfix.serviceplatform.core.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class PagingUtils {

    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "size";

    private PagingUtils() {}

    public static <T> List<T> findWithPageable(Function<Pageable, List<T>> entityPageableListSupplier, Supplier<List<T>> entityListSupplier,
                                               Integer page, Integer size) {
        Optional<Pageable> optionalPageable = of(page, size);
        return optionalPageable
                .map(entityPageableListSupplier)
                .orElse(entityListSupplier.get());
    }

    private static Optional<Pageable> of(Integer page, Integer size) {
        if (page == null || size == null) {
            return Optional.empty();
        } else {
            Pageable pageable = PageRequest.of(page, size);
            return Optional.of(pageable);
        }
    }
}
