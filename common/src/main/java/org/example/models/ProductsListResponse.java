package org.example.models;

import java.util.List;

public record ProductsListResponse(
        List<ProductItem> goods
) {
}
