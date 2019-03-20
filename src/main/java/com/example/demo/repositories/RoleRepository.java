package com.example.demo.repositories;

import com.example.demo.beans.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
  Role findByRole(String role);
}
