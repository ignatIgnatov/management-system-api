package com.system.management.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.system.management.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  private UserEntity user;
  private BCryptPasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    user = createUser(passwordEncoder);
  }

  @Test
  void testSaveUser() {
    userRepository.save(user);

    assertEquals(1, userRepository.count());
  }

  private static UserEntity createUser(BCryptPasswordEncoder passwordEncoder) {
    passwordEncoder = new BCryptPasswordEncoder();
    String password = passwordEncoder.encode("p@5w0rd");
    UserEntity user = new UserEntity();
    user.setEmail("test@test.com");
    user.setPassword(password);
    return user;
  }
}
