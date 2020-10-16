package com.rest.api.repository;

import com.rest.api.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName(value = "uid로 조회시 사용자가 반환된다.")
    @Test
    void whenFindByUidThenReturnUser() {
        // given
        String uid = "test@gmail.com";
        String name = "test";
        String password = "1234";
        User user = User.builder()
                .uid(uid)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findByUid(uid);

        // then
        assertThat(findUser).isNotNull().isPresent();
        assertThat(findUser.get().getName()).isEqualTo(name);
    }
}