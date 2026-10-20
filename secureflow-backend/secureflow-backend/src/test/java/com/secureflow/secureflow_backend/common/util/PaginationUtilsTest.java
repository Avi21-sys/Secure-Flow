package com.secureflow.secureflow_backend.common.util;

import com.secureflow.secureflow_backend.common.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaginationUtilsTest {

    private static final List<String> ALLOWED_FIELDS =
            List.of("id", "title", "severity", "status", "createdAt");

    @Test
    void validatePage_shouldAcceptZero() {
        assertEquals(0, PaginationUtils.validatePage(0));
    }

    @Test
    void validatePage_shouldAcceptPositivePage() {
        assertEquals(5, PaginationUtils.validatePage(5));
    }

    @Test
    void validatePage_shouldRejectNegativePage() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> PaginationUtils.validatePage(-1)
        );

        assertEquals("Page number must not be negative", exception.getMessage());
    }

    @Test
    void validateSize_shouldAcceptMinimumAndMaximum() {
        assertEquals(1, PaginationUtils.validateSize(1));
        assertEquals(100, PaginationUtils.validateSize(100));
    }

    @Test
    void validateSize_shouldRejectZeroOrNegativeSize() {
        assertThrows(
                InvalidRequestException.class,
                () -> PaginationUtils.validateSize(0)
        );

        assertThrows(
                InvalidRequestException.class,
                () -> PaginationUtils.validateSize(-1)
        );
    }

    @Test
    void validateSize_shouldRejectSizeAbove100() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> PaginationUtils.validateSize(101)
        );

        assertEquals("Page size must not exceed 100", exception.getMessage());
    }

    @Test
    void validateSortField_shouldAcceptAllowedField() {
        assertEquals(
                "title",
                PaginationUtils.validateSortField(
                        "title", ALLOWED_FIELDS, "createdAt"
                )
        );
    }

    @Test
    void validateSortField_shouldTrimAllowedField() {
        assertEquals(
                "title",
                PaginationUtils.validateSortField(
                        " title ", ALLOWED_FIELDS, "createdAt"
                )
        );
    }

    @Test
    void validateSortField_shouldFallbackForInvalidOrBlankField() {
        assertEquals(
                "createdAt",
                PaginationUtils.validateSortField(
                        "unknownField", ALLOWED_FIELDS, "createdAt"
                )
        );

        assertEquals(
                "createdAt",
                PaginationUtils.validateSortField(
                        "   ", ALLOWED_FIELDS, "createdAt"
                )
        );

        assertEquals(
                "createdAt",
                PaginationUtils.validateSortField(
                        null, ALLOWED_FIELDS, "createdAt"
                )
        );
    }

    @Test
    void validateSortDirection_shouldAcceptAscAndDescCaseInsensitively() {
        assertEquals(
                Sort.Direction.ASC,
                PaginationUtils.validateSortDirection("asc")
        );

        assertEquals(
                Sort.Direction.ASC,
                PaginationUtils.validateSortDirection(" ASC ")
        );

        assertEquals(
                Sort.Direction.DESC,
                PaginationUtils.validateSortDirection("desc")
        );

        assertEquals(
                Sort.Direction.DESC,
                PaginationUtils.validateSortDirection("DESC")
        );
    }

    @Test
    void validateSortDirection_shouldDefaultToDescForNullOrBlank() {
        assertEquals(
                Sort.Direction.DESC,
                PaginationUtils.validateSortDirection(null)
        );

        assertEquals(
                Sort.Direction.DESC,
                PaginationUtils.validateSortDirection("   ")
        );
    }

    @Test
    void validateSortDirection_shouldRejectInvalidDirection() {
        InvalidRequestException exception = assertThrows(
                InvalidRequestException.class,
                () -> PaginationUtils.validateSortDirection("sideways")
        );

        assertEquals(
                "Sort direction must be 'asc' or 'desc'",
                exception.getMessage()
        );
    }
}
