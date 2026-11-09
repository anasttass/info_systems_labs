package ru.stud;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.stud.dto.*;
import ru.stud.model.*;
import ru.stud.util.UserMapper;
import static org.assertj.core.api.Assertions.assertThat;

class AuthDtoTest {
    @Test
    void validatesCredentialsAndUtf8PasswordBoundaries() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            for (String login : new String[]{null, "", " \t"}) {
                assertThat(validator.validate(new LoginRequest(login, "p"))).isNotEmpty();
                assertThat(validator.validate(new RegisterRequest(login, "p", null, null))).isNotEmpty();
            }
            for (String password : new String[]{null, ""}) {
                assertThat(validator.validate(new LoginRequest("u", password))).isNotEmpty();
                assertThat(validator.validate(new RegisterRequest("u", password, null, null))).isNotEmpty();
            }
            assertThat(validator.validate(new RegisterRequest("u", "   ", null, null))).isNotEmpty();
            for (String password : new String[]{"a".repeat(72), "я".repeat(36)}) {
                assertThat(validator.validate(new RegisterRequest("u", password, null, null))).isEmpty();
            }
            for (String password : new String[]{"a".repeat(73), "я".repeat(36) + "a"}) {
                assertThat(validator.validate(new RegisterRequest("u", password, null, null))).isNotEmpty();
                assertThat(validator.validate(new LoginRequest("u", password))).isEmpty();
            }
            // Login retains compatibility with an existing whitespace-only password.
            assertThat(validator.validate(new LoginRequest("u", " "))).isEmpty();
        }
    }

    @Test
    void mapperKeepsUntrustedRoleAndCredentialsOutOfEntity() throws Exception {
        UserMapper mapper = Mappers.getMapper(UserMapper.class);
        User user = mapper.toEntity(new RegisterRequest("u", "secret", UserRoles.ADMIN, "admin-secret"));
        assertThat(user.getLogin()).isEqualTo("u");
        assertThat(user.getId()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRole()).isNull();
        user.setId(7L);
        user.setRole(UserRoles.GENERAL);
        user.setPassword("hash-secret");
        ObjectMapper json = new ObjectMapper();
        var response = json.readTree(json.writeValueAsString(mapper.toResponse(user)));
        assertThat(response.size()).isEqualTo(3);
        assertThat(response.has("id") && response.has("login") && response.has("role")).isTrue();
        assertThat(new RegisterRequest("u", "secret", null, "admin-secret").toString())
                .doesNotContain("admin-secret", "secret");
        assertThat(new LoginRequest("u", "secret").toString()).doesNotContain("secret");
    }
}
