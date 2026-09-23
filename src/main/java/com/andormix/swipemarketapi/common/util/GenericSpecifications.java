package com.andormix.swipemarketapi.common.util;

import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class GenericSpecifications {

    private GenericSpecifications() {
        // Clase de utilidad no instanciable
    }

    /**
     * Búsqueda por texto (LIKE %search%) ignorando mayúsculas/minúsculas.
     */
    public static <T> Specification<T> containsText(String fieldName, String value) {
        return (root, query, builder) -> {
            if (value == null || value.isBlank()) {
                return builder.conjunction(); // Equivalente a "1=1" (no filtra)
            }
            return builder.like(
                    builder.lower(root.get(fieldName)),
                    "%" + value.trim().toLowerCase() + "%"
            );
        };
    }

    /**
     * Igualdad exacta (field = value). Sirve para Enums, Strings, Longs, Booleans, etc.
     */
    public static <T, V> Specification<T> isEqualTo(String fieldName, V value) {
        return (root, query, builder) -> {
            if (value == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get(fieldName), value);
        };
    }

    /**
     * Rango: Mayor o igual que (field >= value).
     */
    public static <T, V extends Comparable<? super V>> Specification<T> greaterThanOrEqualTo(String fieldName, V value) {
        return (root, query, builder) -> {
            if (value == null) {
                return builder.conjunction();
            }
            return builder.greaterThanOrEqualTo(root.get(fieldName), value);
        };
    }

    /**
     * Rango: Menor o igual que (field <= value).
     */
    public static <T, V extends Comparable<? super V>> Specification<T> lessThanOrEqualTo(String fieldName, V value) {
        return (root, query, builder) -> {
            if (value == null) {
                return builder.conjunction();
            }
            return builder.lessThanOrEqualTo(root.get(fieldName), value);
        };
    }

    /**
     * Navegación por relación (JOIN) para comprobar igualdad (ej: product.seller = user).
     */
    public static <T, V> Specification<T> joinIsEqualTo(String joinField, String fieldName, V value) {
        return (root, query, builder) -> {
            if (value == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get(joinField).get(fieldName), value);
        };
    }
}