package net.myapplication.myapp.object.product.constants;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ProductSortField {

    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    NAME("name"),
    PRICE("price"),
    RATING("rating"),
    REVIEW_COUNT("reviewCount"),
    STOCK("stock");

    private final String property;

    ProductSortField(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }


    public static boolean isAllowed(String property) {

        return Arrays.stream(values())
                .anyMatch(
                    field ->
                        field.property.equals(property)
                );
    }


    public static Set<String> allowedProperties() {

        return Arrays.stream(values())
                .map(ProductSortField::getProperty)
                .collect(Collectors.toUnmodifiableSet());
    }
}