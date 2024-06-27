package com.rodrigues.silva.marcos.ms.auth_api.core.repository;

import com.rodrigues.silva.marcos.ms.auth_api.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);
}
