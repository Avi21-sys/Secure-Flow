package com.secureflow.secureflow_backend.incident.service;

import com.secureflow.secureflow_backend.audit.service.AuditService;
import com.secureflow.secureflow_backend.common.dto.PagedResponse;
import com.secureflow.secureflow_backend.common.exception.InvalidRequestException;
import com.secureflow.secureflow_backend.incident.dto.IncidentResponse;
import com.secureflow.secureflow_backend.incident.entity.Incident;
import com.secureflow.secureflow_backend.incident.entity.IncidentSeverity;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import com.secureflow.secureflow_backend.incident.repository.IncidentRepository;
import com.secureflow.secureflow_backend.user.repository.UserRepository;
import com.secureflow.secureflow_backend.vulnerability.entity.Vulnerability;
import com.secureflow.secureflow_backend.vulnerability.repository.VulnerabilityRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private VulnerabilityRespository vulnerabilityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    private Vulnerability vulnerability;
    private Incident incident;

    @BeforeEach
    void setUp() {
        vulnerability = Vulnerability.builder()
                .id(10L)
                .title("SQL Injection")
                .build();

        incident = Incident.builder()
                .id(1L)
                .title("Production security incident")
                .description("Potential exploitation detected")
                .severity(IncidentSeverity.P2_HIGH)
                .status(IncidentStatus.OPEN)
                .vulnerability(vulnerability)
                .createdAt(LocalDateTime.of(2026, 1, 15, 10, 30))
                .build();
    }

    @Test
    void getIncidents_shouldReturnContentAndPaginationMetadata() {
        Page<Incident> pageResult = new PageImpl<>(
                List.of(incident),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                21
        );

        when(incidentRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(pageResult);

        PagedResponse<IncidentResponse> result =
                incidentService.getIncidents(
                        0, 10, "createdAt", "desc",
                        null, null, null, null
                );

        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(21, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        assertTrue(result.isFirst());
        assertFalse(result.isLast());
        assertEquals(1, result.getContent().size());

        IncidentResponse response = result.getContent().get(0);
        assertEquals(1L, response.getId());
        assertEquals("Production security incident", response.getTitle());
        assertEquals(IncidentSeverity.P2_HIGH, response.getSeverity());
        assertEquals(IncidentStatus.OPEN, response.getStatus());
        assertEquals(10L, response.getVulnerabilityId());
        assertEquals("SQL Injection", response.getVulnerabilityTitle());
        assertNull(response.getAssignedUserId());
        assertNull(response.getAssignedUserName());
    }

    @Test
    void getIncidents_shouldPassRequestedPageSizeAndSort() {
        Page<Incident> pageResult = new PageImpl<>(
                List.of(),
                PageRequest.of(2, 5, Sort.by(Sort.Direction.ASC, "title")),
                0
        );

        when(incidentRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(pageResult);

        incidentService.getIncidents(
                2, 5, "title", "asc",
                null, null, null, null
        );

        ArgumentCaptor<Pageable> captor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(incidentRepository).findAll(
                any(Specification.class),
                captor.capture()
        );

        Pageable pageable = captor.getValue();

        assertEquals(2, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(
                Sort.Direction.ASC,
                pageable.getSort().getOrderFor("title").getDirection()
        );
    }

    @Test
    void getIncidents_shouldFallbackForUnsupportedSortField() {
        when(incidentRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        incidentService.getIncidents(
                0, 10, "password", "desc",
                null, null, null, null
        );

        ArgumentCaptor<Pageable> captor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(incidentRepository).findAll(
                any(Specification.class),
                captor.capture()
        );

        Sort sort = captor.getValue().getSort();

        assertNotNull(sort.getOrderFor("createdAt"));
        assertNull(sort.getOrderFor("password"));
    }

    @Test
    void getIncidents_shouldRejectNegativePage() {
        assertThrows(
                InvalidRequestException.class,
                () -> incidentService.getIncidents(
                        -1, 10, "createdAt", "desc",
                        null, null, null, null
                )
        );

        verifyNoInteractions(incidentRepository);
    }

    @Test
    void getIncidents_shouldRejectPageSizeAbove100() {
        assertThrows(
                InvalidRequestException.class,
                () -> incidentService.getIncidents(
                        0, 101, "createdAt", "desc",
                        null, null, null, null
                )
        );

        verifyNoInteractions(incidentRepository);
    }

    @Test
    void getIncidents_shouldRejectInvalidSortDirection() {
        assertThrows(
                InvalidRequestException.class,
                () -> incidentService.getIncidents(
                        0, 10, "createdAt", "sideways",
                        null, null, null, null
                )
        );

        verifyNoInteractions(incidentRepository);
    }

    @Test
    void getIncidents_shouldAcceptAllOptionalFilters() {
        when(incidentRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        assertDoesNotThrow(() ->
                incidentService.getIncidents(
                        0, 10, "createdAt", "desc",
                        IncidentStatus.OPEN,
                        IncidentSeverity.P2_HIGH,
                        10L,
                        20L
                )
        );

        verify(incidentRepository).findAll(
                any(Specification.class),
                any(Pageable.class)
        );
    }
}
