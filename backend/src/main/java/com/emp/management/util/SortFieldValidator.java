package com.emp.management.util;

import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Set;

/**
 * Utility class for validating and safely handling sort fields.
 * Prevents SQL injection and invalid sort parameters.
 */
public class SortFieldValidator {

    // Define allowed sort fields for each entity
    private static final Map<String, Set<String>> ALLOWED_FIELDS = Map.ofEntries(
            Map.entry("leave", Set.of("id", "createdAt", "status", "employeeId", "reason", "startDate", "endDate")),
            Map.entry("employee", Set.of("id", "firstName", "lastName", "email", "phone", "designation", "dateOfJoining", "salary", "active")),
            Map.entry("attendance", Set.of("id", "date", "status", "employeeId", "createdAt")),
            Map.entry("department", Set.of("id", "name", "description", "createdAt")),
            Map.entry("user", Set.of("id", "email", "role", "active", "createdAt"))
    );

    /**
     * Validates and sanitizes sort field for a given entity type.
     * Returns the field if valid, otherwise returns the default "createdAt".
     */
    public static String validateSortField(String entityType, String sortBy) {
        Set<String> allowedFields = ALLOWED_FIELDS.getOrDefault(entityType, Set.of());
        if (allowedFields.isEmpty() || !allowedFields.contains(sortBy)) {
            return "createdAt"; // Default to safe field
        }
        return sortBy;
    }

    /**
     * Creates a Sort object with validated sort field and direction.
     */
    public static Sort createSort(String entityType, String sortBy, String sortDir) {
        String validatedField = validateSortField(entityType, sortBy);
        return sortDir.equalsIgnoreCase("asc")
                ? Sort.by(validatedField).ascending()
                : Sort.by(validatedField).descending();
    }
}
