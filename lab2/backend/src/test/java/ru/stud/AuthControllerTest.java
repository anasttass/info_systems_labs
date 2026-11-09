package ru.stud;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import ru.stud.config.SecurityConfig;
import ru.stud.controller.AuthController;
import ru.stud.dto.*;
import ru.stud.exception.GlobalExceptionHandler;
import ru.stud.model.UserRoles;
import ru.stud.service.UserService;
import ru.stud.tokens.JwtService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class AuthControllerTest {
    @MockBean private UserService users;
    @MockBean private JwtService tokens;
    @MockBean private JwtDecoder decoder;
    @Autowired private MockMvc mvc;

    @Test
    void rejectsInvalidBodiesWithoutCallingServicesOrEchoingSecrets() throws Exception {
        for (String endpoint : new String[]{"login", "register"}) {
            for (String body : new String[]{"{}", "null", "{", "{\"login\":\" \",\"password\":\"secret-value\"}"}) {
                mvc.perform(post("/auth/" + endpoint).contentType("application/json").content(body))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("message").isString())
                        .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("secret-value"))));
            }
        }
        mvc.perform(post("/auth/register").contentType("application/json")
                .content("{\"login\":\"u\",\"password\":\"secret-value\",\"role\":\"ROOT-secret\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("ROOT-secret"))));
        verifyNoInteractions(users, tokens);
    }

    @Test
    void preservesSuccessfulHttpContracts() throws Exception {
        mvc.perform(post("/auth/register").contentType("application/json")
                .content("{\"id\":99,\"login\":\"u\",\"password\":\"p\"}"))
                .andExpect(status().isCreated()).andExpect(content().string(""));
        verify(users).createNewUser(new RegisterRequest("u", "p", null, null));
        UserResponse user = new UserResponse(7L, "u", UserRoles.GENERAL);
        when(users.signIn("u", "p")).thenReturn(user);
        when(tokens.generateToken(user)).thenReturn("jwt");
        mvc.perform(post("/auth/login").contentType("application/json")
                .content("{\"login\":\"u\",\"password\":\"p\",\"role\":\"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("token").value("jwt"))
                .andExpect(jsonPath("user_id").value(7))
                .andExpect(jsonPath("login").value("u"))
                .andExpect(jsonPath("role").value("GENERAL"));
    }
}
