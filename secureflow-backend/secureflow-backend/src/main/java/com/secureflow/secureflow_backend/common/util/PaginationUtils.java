package com.secureflow.secureflow_backend.common.util;

import com.secureflow.secureflow_backend.common.exception.InvalidRequestException;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PaginationUtils {

    private PaginationUtils() {
    }

    public static int validatePage(int page) {
        if (page < 0) {
            throw new InvalidRequestException(
                    "Page number must not be negative"
            );
        }
        return page;
    }

    public static int validateSize(int size) {
        if (size < 1) {
            throw new InvalidRequestException(
                    "Page size must be at least 1"
            );
        }
        if (size > 100) {
            throw new InvalidRequestException(
                    "Page size must not exceed 100"
            );
        }
        return size;
    }

    public static String validateSortField(
            String sortBy,
            List<String> allowedFields,
            String defaultField
    ) {
        if (sortBy == null || sortBy.isBlank()) {
            return defaultField;
        }

        String trimmedSortBy = sortBy.trim();

        if (!allowedFields.contains(trimmedSortBy)) {
            return defaultField;
        }

        return trimmedSortBy;
    }

    public static Sort.Direction validateSortDirection(String sortDir) {
        if (sortDir == null || sortDir.isBlank()) {
            return Sort.Direction.DESC;
        }

        String normalized = sortDir.trim().toLowerCase();

        if ("asc".equals(normalized)) {
            return Sort.Direction.ASC;
        } else if ("desc".equals(normalized)) {
            return Sort.Direction.DESC;
        } else {
            throw new InvalidRequestException(
                    "Sort direction must be 'asc' or 'desc'"
            );
        }
    }
}
