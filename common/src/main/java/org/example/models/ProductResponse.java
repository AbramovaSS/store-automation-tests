package org.example.models;

import java.util.Map;

public record ProductResponse(
        Map<String, Integer> data,
        String message
) {
}
