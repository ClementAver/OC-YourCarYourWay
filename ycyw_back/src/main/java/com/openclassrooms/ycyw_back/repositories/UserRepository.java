package com.openclassrooms.ycyw_back.repositories;

import com.openclassrooms.ycyw_back.entities.User;
import com.openclassrooms.ycyw_back.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByRole(Role role);
}