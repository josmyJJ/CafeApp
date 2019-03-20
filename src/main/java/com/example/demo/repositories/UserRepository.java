package com.example.demo.repositories;

import com.example.demo.beans.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  User findByUsername(String username);
  User findByEmail(String email);
  Long countByEmail(String email);
  Long countByUsername(String username);
}
