package com.jadenx.kxgigservice;

import com.jadenx.kxgigservice.config.security.JwtAuthenticationConfig;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = HomeController.class)
@Import({JwtAuthenticationConfig.class})
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    @DisplayName(
        "Given an anonymous user, "
            + "when a secured endpoint without pre-authorization is accessed, it returns status 401")
    void shouldDenyAccessToAnonymousUserOnEndpointWithoutPreAuthorization() throws Exception {
        mockMvc
            .perform(get("/authenticated"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "Jacek")
    @DisplayName(
        "Given an authenticated user without authorities, "
            + "when a secured endpoint without pre-authorization is accessed,"
            + " it returns status 200 and its message")
     void shouldProvideAccessToAuthenticatedUserWithoutAuthoritiesOnEndpointWithoutPreAuthorization() throws Exception {
        mockMvc
            .perform(get("/authenticated"))
            .andExpect(status().isOk())
            .andExpect(content().string("You're authenticated, Jacek!"));
    }
}
