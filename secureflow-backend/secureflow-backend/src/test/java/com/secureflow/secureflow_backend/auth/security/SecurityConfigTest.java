
package com.secureflow.secureflow_backend.auth.security;

import com.secureflow.secureflow_backend.incident.controller.IncidentController;
import com.secureflow.secureflow_backend.incident.service.IncidentService;

import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = IncidentController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncidentService incidentService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private static final String TEST_URL =
            "/api/v1/incidents/999999";

    @Test
    void shouldReturn401WhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturn403WhenUserDoesNotHaveAllowedRole() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DEVELOPER")
    void shouldPassAuthorizationForAllowedRole() throws Exception {
        mockMvc.perform(get(TEST_URL))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401WhenJwtIsMalformed() throws Exception {
        when(jwtService.extractUsername("not-a-jwt"))
                .thenThrow(new MalformedJwtException("Malformed token"));

        mockMvc.perform(get(TEST_URL)
                        .header("Authorization", "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void shouldReturn401WhenJwtIsExpired() throws Exception {
        when(jwtService.extractUsername("expired-token"))
                .thenThrow(
                        new io.jsonwebtoken.ExpiredJwtException(
                                null,
                                null,
                                "JWT has expired"
                        )
                );

        mockMvc.perform(get(TEST_URL)
                        .header("Authorization", "Bearer expired-token"))
                .andExpect(status().isUnauthorized());
    }

}
