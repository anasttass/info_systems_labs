package ru.stud;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.stud.dto.*;
import ru.stud.model.*;
import ru.stud.repository.UserRepository;
import ru.stud.service.UserService;
import ru.stud.util.UserMapper;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private final UserRepository repository = mock(UserRepository.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private UserService service(String code) {
        return new UserService(Mappers.getMapper(UserMapper.class), encoder, repository, code);
    }

    @Test
    void assignsValidatedRoleAndHashesPasswordWithoutTrimming() {
        UserService service = service("code");
        service.createNewUser(new RegisterRequest("u", " p ", null, null));
        service.createNewUser(new RegisterRequest("a", "p", UserRoles.ADMIN, "code"));
        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(repository, times(2)).save(saved.capture());
        User general = saved.getAllValues().get(0);
        assertThat(general.getRole()).isEqualTo(UserRoles.GENERAL);
        assertThat(general.getId()).isNull();
        assertThat(general.getPassword()).isNotEqualTo(" p ");
        assertThat(encoder.matches(" p ", general.getPassword())).isTrue();
        assertThat(saved.getAllValues().get(1).getRole()).isEqualTo(UserRoles.ADMIN);
    }

    @Test
    void rejectsDuplicateAndInvalidAdminRegistrationWithoutSaving() {
        UserService service = service("code");
        for (String code : new String[]{null, "", "wrong"}) {
            assertThatThrownBy(() -> service.createNewUser(new RegisterRequest("a", "p", UserRoles.ADMIN, code)))
                    .isInstanceOf(AccessDeniedException.class);
        }
        assertThatThrownBy(() -> service("").createNewUser(new RegisterRequest("a", "p", UserRoles.ADMIN, "code")))
                .isInstanceOf(IllegalStateException.class);
        when(repository.existsByLogin("u")).thenReturn(true);
        assertThatThrownBy(() -> service.createNewUser(new RegisterRequest("u", "p", null, null)))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void signsInAndReturnsOnlyPublicFields() {
        User user = new User();
        user.setId(7L); user.setLogin("u"); user.setRole(UserRoles.GENERAL);
        user.setPassword(encoder.encode("p"));
        when(repository.findByLogin("u")).thenReturn(Optional.of(user));
        when(repository.findByLogin("missing")).thenReturn(Optional.empty());
        when(repository.findById(7L)).thenReturn(Optional.of(user));
        UserService service = service("code");
        assertThat(service.signIn("u", "p")).isEqualTo(new UserResponse(7L, "u", UserRoles.GENERAL));
        assertThat(service.getUserById(7L)).isEqualTo(service.signIn("u", "p"));
        assertThatThrownBy(() -> service.signIn("u", "wrong")).hasMessage("Неверный логин или пароль");
        assertThatThrownBy(() -> service.signIn("missing", "p")).hasMessage("Неверный логин или пароль");
    }
}
