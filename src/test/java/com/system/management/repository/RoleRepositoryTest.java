package com.system.management.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.system.management.model.RoleEntity;
import com.system.management.model.enums.RoleEnum;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class RoleRepositoryTest {

  @Autowired private RoleRepository roleRepository;

  private RoleEntity admin;
  private RoleEntity user;

  @BeforeEach
  void setUp() {
    roleRepository.deleteAll();
    admin = createRole(RoleEnum.ADMIN);
    user = createRole(RoleEnum.USER);
  }

  @Test
  void testCreateRole() {
    roleRepository.saveAll(List.of(admin, user));

    assertEquals(2, roleRepository.count());
  }

  private RoleEntity createRole(RoleEnum role) {
    RoleEntity roleEntity = new RoleEntity();
    roleEntity.setRole(role);
    return roleEntity;
  }
}
